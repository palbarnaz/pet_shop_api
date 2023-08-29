package com.example.demo.builders.dtos;

import com.example.demo.dtos.CreateService;

public class CreateServiceBuilder {
    private String description = "Consulta Veterinaria";
    private final int duration = 1;
    private double price = 50;

    private CreateServiceBuilder() {
    }

    public static CreateServiceBuilder init() {
        return new CreateServiceBuilder();
    }

    public CreateServiceBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public CreateServiceBuilder withPrice(double price) {
        this.price = price;
        return this;
    }

    public CreateService builder() {
        return new CreateService(
                description,
                duration,
                price
        );
    }


}
