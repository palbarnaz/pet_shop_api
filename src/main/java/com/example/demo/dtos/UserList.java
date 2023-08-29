package com.example.demo.dtos;

import com.example.demo.enums.Profile;
import com.example.demo.models.User;

import java.util.UUID;

public record UserList(UUID id, String name, String email, String phone, String password, Profile profile) {
    public UserList(User user) {
        this(user.getId(), user.getName(), user.getEmail(), user.getPhone(), user.getPassword(), user.getProfile());
    }
}
