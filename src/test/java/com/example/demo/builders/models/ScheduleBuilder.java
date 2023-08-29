package com.example.demo.builders.models;

import com.example.demo.models.Animal;
import com.example.demo.models.Schedule;
import com.example.demo.models.Service;
import com.example.demo.models.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component

public class ScheduleBuilder {
    private UUID id;
    private LocalDateTime dateHour = LocalDateTime.now();
    private User user;
    private Animal animal;
    private Service service;


    private ScheduleBuilder() {
    }

    public static ScheduleBuilder init() {
        return new ScheduleBuilder();
    }

    public ScheduleBuilder withDateHour(LocalDateTime dateHour) {
        this.dateHour = dateHour;
        return this;
    }

    public ScheduleBuilder withUser(User user) {
        this.user = user;
        return this;
    }

    public ScheduleBuilder withAnimal(Animal animal) {
        this.animal = animal;
        return this;
    }

    public ScheduleBuilder withService(Service service) {
        this.service = service;
        return this;
    }

    public Schedule builder() {
        return new Schedule(
                null,
                dateHour,
                user,
                animal,
                service
        );
    }

}
