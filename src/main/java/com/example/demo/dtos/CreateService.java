package com.example.demo.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateService(
        @NotBlank
        String description,
        @NotNull
        int duration,

        @NotNull
        double price
) {
}
