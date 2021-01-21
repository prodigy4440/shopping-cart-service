package com.fahdisa.scs.core;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.fahdisa.scs.api.ApiResponse;
import com.fahdisa.scs.api.ChangePassword;
import com.fahdisa.scs.api.Login;
import com.fahdisa.scs.api.Status;
import com.fahdisa.scs.db.user.User;
import com.fahdisa.scs.db.user.UserStore;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class UserService {

    private UserStore userStore;
    private KeyService keyService;

    public UserService(UserStore userStore, KeyService keyService) {
        this.userStore = userStore;
        this.keyService = keyService;
    }

    public ApiResponse create(User user) {

        if (StringUtils.isBlank(user.getRole())) {
            user.setRole("NORMAL");
        }

        Optional<User> optional = userStore.findByEmail(user.getEmail());

        if (optional.isPresent()) {
            return new ApiResponse.Builder<User>()
                    .status(Status.FAILED)
                    .description("User already exist")
                    .build();
        }
        String pwHash = BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray());
        user.setPassword(pwHash);
        user.setId(null);
        userStore.save(user);
        return new ApiResponse.Builder<User>()
                .status(Status.SUCCESS)
                .description("Success")
                .data(user)
                .build();
    }

    public ApiResponse<User> login(Login login) {
        Optional<User> optional = userStore.findByEmail(login.getUsername());
        if (!optional.isPresent()) {
            return new ApiResponse.Builder<User>()
                    .status(Status.INVALID_AUTHENTICATION)
                    .description("Invalid authentication")
                    .build();
        }
        User user = optional.get();
        if (!BCrypt.verifyer().verify(login.getPassword().toCharArray(), user.getPassword()).verified) {
            return new ApiResponse.Builder<User>()
                    .status(Status.INVALID_AUTHENTICATION)
                    .description("Invalid authentication")
                    .build();
        }

        Date tokenExpiration = Date.from(Instant.now().plusSeconds(60 * 30));
        String jwt = Jwts.builder()
                .setSubject(user.getId().toString())
                .signWith(keyService.getKey())
                .setExpiration(tokenExpiration)
                .setIssuedAt(new Date())
                .setIssuer("shopper-cart-service")
                .claim("role", user.getRole())
                .compact();

        return new ApiResponse.Builder<User>()
                .status(Status.SUCCESS)
                .description("Success")
                .data(jwt)
                .build();
    }

    public ApiResponse<User> find(String id) {
        Optional<User> optional = userStore.findById(id);
        if (!optional.isPresent()) {
            return new ApiResponse.Builder<User>()
                    .status(Status.FAILED)
                    .description("User not found")
                    .build();
        }
        return new ApiResponse.Builder<User>()
                .status(Status.SUCCESS)
                .description("Success")
                .data(optional.get())
                .build();
    }

    public Optional<User> findById(String id) {
        return userStore.findById(id);
    }

    public ApiResponse<User> updatePassword(String id, ChangePassword changePassword) {
        Optional<User> optional = userStore.findById(id);
        if (!optional.isPresent()) {
            return new ApiResponse.Builder<User>()
                    .status(Status.FAILED)
                    .description("User not found")
                    .build();
        }

        User user = optional.get();
        if (!BCrypt.verifyer().verify(changePassword.getCurrentPassword().toCharArray(), user.getPassword()).verified) {
            return new ApiResponse.Builder<User>()
                    .status(Status.FAILED)
                    .description("Wrong password")
                    .build();
        }

        if (!userStore.updatePassword(id, changePassword.getNewPassword())) {
            return new ApiResponse.Builder<User>()
                    .status(Status.FAILED)
                    .description("Unable to update record")
                    .build();
        }

        return new ApiResponse.Builder<User>()
                .status(Status.SUCCESS)
                .description("Success")
                .data(user)
                .build();

    }

    public ApiResponse<List<User>> findAll(int page, int size) {
        if (page < 0) {
            page = 0;
        }

        if (size < 1) {
            size = 1;
        }
        return new ApiResponse.Builder<List<User>>()
                .status(Status.SUCCESS)
                .description("Success")
                .data(userStore.findAll(page, size))
                .build();
    }

}
