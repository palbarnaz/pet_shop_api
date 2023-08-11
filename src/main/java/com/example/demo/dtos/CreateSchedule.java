package com.example.demo.dtos;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateSchedule(
        @NotNull
        @FutureOrPresent
        LocalDateTime dateHour,
        @NotNull
        UUID idAnimal,


        @NotNull
        UUID idService

) {
}
