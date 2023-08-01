package com.example.demo.dtos;

import com.example.demo.enums.Profile;

public record ResponseLogin(String tokenLogin, Enum<Profile> profile) {
}
