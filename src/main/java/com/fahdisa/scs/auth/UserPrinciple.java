package com.fahdisa.scs.auth;

import com.fahdisa.scs.db.user.User;

import java.security.Principal;
import java.util.Objects;

public class UserPrinciple implements Principal {

    private User user;

    public UserPrinciple() {
    }

    public UserPrinciple(User user) {
        this.user = user;
    }

    @Override
    public String getName() {
        return user.getId().toString();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPrinciple that = (UserPrinciple) o;
        return Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user);
    }
}
