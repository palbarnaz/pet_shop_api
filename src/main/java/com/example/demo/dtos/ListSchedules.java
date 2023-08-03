package com.example.demo.dtos;

import com.example.demo.models.Animal;
import com.example.demo.models.Service;

import java.time.LocalDateTime;
import java.util.UUID;

public record ListSchedules(
        UUID id,
        LocalDateTime dateHour,
        ListSchedulesUser user,
        Animal animal,
        Service service

) {


}
