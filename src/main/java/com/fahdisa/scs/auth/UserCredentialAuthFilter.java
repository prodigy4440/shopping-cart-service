package com.fahdisa.scs.auth;

import io.dropwizard.auth.AuthFilter;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.security.Principal;

@Priority((Priorities.AUTHENTICATION))
public class UserCredentialAuthFilter<P extends Principal> extends AuthFilter<JwtHolder, P> {

    public static class Builder<P extends Principal> extends AuthFilterBuilder<JwtHolder, P, UserCredentialAuthFilter<P>> {
        @Override
        protected UserCredentialAuthFilter<P> newInstance() {
            return new UserCredentialAuthFilter<>();
        }
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        final JwtHolder credential =
                getCredentials(containerRequestContext.getHeaders().getFirst(HttpHeaders.AUTHORIZATION));
        if (!authenticate(containerRequestContext, credential, "JWT")) {
            throw new WebApplicationException(this.unauthorizedHandler.buildResponse(this.prefix, this.realm));
        }
    }

    private static JwtHolder getCredentials(String authLine) {
        if (authLine != null && authLine.startsWith("Bearer ")) {
            JwtHolder result = new JwtHolder();
            result.setToken(authLine.substring(7));
            return result;
        }
        return null;
    }
}
