package com.example.demo.dtos;

import com.example.demo.enums.Profile;
import com.example.demo.models.User;

import java.util.UUID;

public record ResponseUser(
        UUID id,
        String name,
        String email,
        String phone,
        Profile profile


) {

    public ResponseUser(User user) {
        this(user.getId(), user.getName(), user.getEmail(), user.getPhone(), user.getProfile());
    }
}
