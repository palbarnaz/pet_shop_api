package com.example.demo.controllers;

import com.example.demo.config.HandleException;
import com.example.demo.dtos.CreateAnimal;
import com.example.demo.dtos.ResponseAnimal;
import com.example.demo.enums.Profile;
import com.example.demo.models.Animal;
import com.example.demo.models.User;
import com.example.demo.repositories.AnimalRepository;
import com.example.demo.repositories.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")

@RestController
@RequestMapping("/animals")
public class AnimalController {
    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping()
    @Transactional
    public ResponseEntity createAnimal(@AuthenticationPrincipal User userLogged, @RequestBody @Valid CreateAnimal newAnimal) {

        var user = userRepository.findById(userLogged.getId());
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body(new HandleException.ErrorData("user", "id inválido"));
        }

        if (user.get().getProfile() == Profile.ADMIN)
            return ResponseEntity.badRequest().body(new HandleException.ErrorData("animal", "Administrador não pode cadastrar animal!"));

        var existAnimal = user.get().getAnimals().stream().filter(a -> a.getName().equalsIgnoreCase(newAnimal.name())).toList();

        if (existAnimal.size() > 0) {
            return ResponseEntity.badRequest().body(new HandleException.ErrorData("animal", "Já existe um pet com esse nome!"));
        }

        var animal = new Animal(newAnimal, user.get().getId());

        animalRepository.save(animal);

        return ResponseEntity.ok(new ResponseAnimal(animal.getId(), animal.getName(), animal.getSpecie(), animal.getUserId()));

    }
}
