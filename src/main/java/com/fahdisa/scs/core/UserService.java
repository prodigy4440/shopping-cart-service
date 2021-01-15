package com.fahdisa.scs.core;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.fahdisa.scs.api.ApiResponse;
import com.fahdisa.scs.api.ChangePassword;
import com.fahdisa.scs.api.Login;
import com.fahdisa.scs.api.Status;
import com.fahdisa.scs.db.user.User;
import com.fahdisa.scs.db.user.UserStore;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;

public class UserService {

    private UserStore userStore;

    public UserService(UserStore userStore) {
        this.userStore = userStore;
    }

    public ApiResponse create(User user) {

        if (StringUtils.isBlank(user.getRole())) {
            user.setRole("NORMAL");
        }

        String pwHash = BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray());
        user.setPassword(pwHash);
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
        return new ApiResponse.Builder<User>()
                .status(Status.SUCCESS)
                .description("Success")
                .data(user)
                .build();
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
