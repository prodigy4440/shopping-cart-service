package com.fahdisa.scs.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;

import java.util.Optional;

public class UserDefaultAuthenticator implements Authenticator<JwtHolder, UserPrinciple> {

    @Override
    public Optional<UserPrinciple> authenticate(JwtHolder jwtHolder) throws AuthenticationException {
        return Optional.of(new UserPrinciple());
    }
}
