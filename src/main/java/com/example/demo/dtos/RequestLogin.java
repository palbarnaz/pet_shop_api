package com.example.demo.dtos;

import jakarta.validation.constraints.NotBlank;

public record RequestLogin(@NotBlank String email, @NotBlank String password) {
}
