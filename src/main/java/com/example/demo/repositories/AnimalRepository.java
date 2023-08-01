package com.example.demo.repositories;

import com.example.demo.models.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AnimalRepository extends JpaRepository<Animal, UUID> {
    Optional<Animal> findByName(String name);
}
