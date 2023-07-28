package com.example.demo.dtos;

import com.example.demo.enums.Profile;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record CreateUser(
        @NotBlank
        @Length(min = 3, max = 100)
        String name,
        @NotBlank
        @Email
        String email,
        @NotBlank
        @Length(min = 11)
         String phone,

        @NotBlank
        @Length(min = 8)
        String password,
        @NotNull
        Profile profile
) {
}
