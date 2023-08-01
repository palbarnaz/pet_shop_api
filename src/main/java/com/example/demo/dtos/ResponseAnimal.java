package com.example.demo.dtos;

import java.util.UUID;

public record ResponseAnimal(
        UUID id,
        String name,
        String specie,
        UUID userId


) {

}
