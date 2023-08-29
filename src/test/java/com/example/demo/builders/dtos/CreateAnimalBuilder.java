package com.example.demo.builders.dtos;

import com.example.demo.dtos.CreateAnimal;

public class CreateAnimalBuilder {
    private String name = "Benicio";

    private String specie = "cachorro";

    private CreateAnimalBuilder() {
    }

    public static CreateAnimalBuilder init() {
        return new CreateAnimalBuilder();
    }

    public CreateAnimalBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public CreateAnimalBuilder withSpecie(String specie) {
        this.specie = specie;
        return this;
    }

    public CreateAnimal builder() {
        return new CreateAnimal(
                name,
                specie
        );
    }

}
