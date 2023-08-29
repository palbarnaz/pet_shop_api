package com.example.demo.builders.dtos;

import com.example.demo.dtos.EditService;

public class UpdateServiceBuilder {
    private final String description = "Vacinacao";
    private final int duration = 1;
    private final double price = 60;


    public static UpdateServiceBuilder init() {
        return new UpdateServiceBuilder();
    }


    public EditService builder() {
        return new EditService(
                description,
                duration,
                price
        );
    }
}
