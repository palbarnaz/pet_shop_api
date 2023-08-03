package com.example.demo.controllers;

import com.example.demo.dtos.CreateAnimal;
import com.example.demo.dtos.ErrorData;
import com.example.demo.dtos.ResponseAnimal;
import com.example.demo.enums.Profile;
import com.example.demo.models.Animal;
import com.example.demo.repositories.AnimalRepository;
import com.example.demo.repositories.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/animals")
public class AnimalController {
    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/{idUser}")
    @Transactional
    public ResponseEntity createAnimal(@RequestBody @Valid CreateAnimal newAnimal, @PathVariable UUID idUser, @RequestHeader("AuthToken") String token) {
        var user = userRepository.findById(idUser);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorData("user", "id inválido"));
        }

        if (user.get().getProfile() == Profile.ADMIN)
            return ResponseEntity.badRequest().body(new ErrorData("animal", "Administrador não pode cadastrar animal!"));


        if (!user.get().isAuthenticated(token)) {
            return ResponseEntity.badRequest().body(new ErrorData("token", "token inválido"));
        }

        if (animalRepository.findByName(newAnimal.name()).isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorData("animal", "Já existe um pet com esse nome!"));
        }


        var animal = new Animal(newAnimal, user.get().getId());


        animalRepository.save(animal);

        return ResponseEntity.ok(new ResponseAnimal(animal.getId(), animal.getName(), animal.getSpecie(), animal.getUserId()));


    }
}
