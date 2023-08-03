package com.example.demo.models;

import com.example.demo.dtos.CreateAnimal;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Entity
@Table(name="animals")
@AllArgsConstructor
@NoArgsConstructor
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String specie;
    @Column(name = "user_id")
    private UUID userId;


    public Animal(CreateAnimal newAnimal, UUID userId) {

        this.name = newAnimal.name();
        this.specie = newAnimal.specie();
        this.userId = userId;
    }


}
