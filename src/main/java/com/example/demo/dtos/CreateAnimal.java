package com.example.demo.dtos;

import jakarta.validation.constraints.NotBlank;

public record CreateAnimal(
        @NotBlank
        String name,
        @NotBlank
        String specie


) {
}
