package com.fahdisa.scs.auth;

import com.fahdisa.scs.core.KeyService;
import com.fahdisa.scs.core.UserService;
import com.fahdisa.scs.db.user.User;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

import java.util.Optional;

public class UserAuthenticator implements Authenticator<JwtHolder, UserPrinciple> {

    private UserService userService;

    private KeyService keyService;

    public UserAuthenticator(UserService userService, KeyService keyService) {
        this.userService = userService;
        this.keyService = keyService;
    }

    @Override
    public Optional<UserPrinciple> authenticate(JwtHolder credentials) throws AuthenticationException {
        try {
            String id = Jwts.parserBuilder().setSigningKey(keyService.getKey())
                    .build().parseClaimsJws(credentials.getToken()).getBody().getSubject();
            Optional<User> optional = userService.findById(id);
            if (!optional.isPresent()) {
                return Optional.empty();
            }
            UserPrinciple user = new UserPrinciple(optional.get());
            return Optional.ofNullable(user);
        } catch (@SuppressWarnings("unused") ExpiredJwtException | UnsupportedJwtException
                | MalformedJwtException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }

}
