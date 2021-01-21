package com.fahdisa.scs.auth;

import io.dropwizard.auth.Authorizer;

public class UserAuthorizer implements Authorizer<UserPrinciple> {

    @Override
    public boolean authorize(UserPrinciple user, String role) {
        return user != null && role.contains(user.getUser().getRole());
    }
}
