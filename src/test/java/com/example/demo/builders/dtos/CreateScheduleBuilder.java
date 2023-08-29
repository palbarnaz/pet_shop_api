package com.example.demo.builders.dtos;

import com.example.demo.dtos.CreateSchedule;

import java.time.LocalDateTime;
import java.util.UUID;

public class CreateScheduleBuilder {


    private LocalDateTime dateHour = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0).plusHours(1);
    private UUID idAnimal = UUID.randomUUID();

    private UUID idService = UUID.randomUUID();

    private CreateScheduleBuilder() {
    }

    public static CreateScheduleBuilder init() {
        return new CreateScheduleBuilder();
    }

    public CreateScheduleBuilder withIdAnimal(UUID idAnimal) {
        this.idAnimal = idAnimal;
        return this;
    }

    public CreateScheduleBuilder withIdService(UUID idService) {
        this.idService = idService;
        return this;
    }

    public CreateScheduleBuilder withDateHour(LocalDateTime dateHour) {
        this.dateHour = dateHour;
        return this;
    }

    public CreateSchedule builder() {
        return new CreateSchedule(
                dateHour,
                idAnimal,
                idService
        );
    }
}
