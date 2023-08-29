package com.example.demo.builders.models;

import com.example.demo.models.Animal;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AnimalBuilder {

    private UUID id;
    private String name = "Benicio";

    private String specie = "cachorro";

    private UUID idUser;

    private AnimalBuilder() {
    }

    public static AnimalBuilder init() {
        return new AnimalBuilder();
    }

    public AnimalBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public AnimalBuilder withSpecie(String specie) {
        this.specie = specie;
        return this;
    }

    public AnimalBuilder withIdUser(UUID idUser) {
        this.idUser = idUser;
        return this;
    }

    public Animal builder() {
        return new Animal(
                null,
                name,
                specie,
                idUser
        );
    }
}
