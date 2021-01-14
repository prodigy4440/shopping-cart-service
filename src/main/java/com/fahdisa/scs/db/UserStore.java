package com.fahdisa.scs.db;

import com.fahdisa.scs.core.User;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class UserStore {

    private Set<User> users = new HashSet<>();

    public UserStore() {

    }

    public Optional<User> saveUser(User user) {
        user.setId(String.valueOf(users.size() + 1));
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        return this.users.add(user) ? Optional.of(user) : Optional.empty();
    }

    public User updateUser(User user) {
        user.setUpdatedAt(new Date());
        this.users.add(user);
        return user;
    }

    public Optional<User> findById(String id) {
        int index = Integer.parseInt(id);
//        this.users
        return null;
    }

}
