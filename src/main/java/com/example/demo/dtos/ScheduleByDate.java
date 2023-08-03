package com.example.demo.dtos;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ScheduleByDate(
        @NotNull
        @FutureOrPresent
        LocalDateTime dateHour
) {
}
