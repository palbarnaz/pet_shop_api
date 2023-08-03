package com.example.demo.dtos;

import com.example.demo.enums.Profile;
import com.example.demo.models.User;

import java.util.UUID;

public record ListSchedulesUser(
        UUID id,
        String name,
        String email,
        String phone,
        Profile profile
) {
    public ListSchedulesUser(User u) {
        this(u.getId(), u.getName(), u.getEmail(), u.getPhone(), u.getProfile());
    }
}
