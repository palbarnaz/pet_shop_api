package com.example.demo.controllers;

import com.example.demo.dtos.RequestLogin;
import com.example.demo.dtos.ResponseLogin;
import com.example.demo.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity login(@RequestBody @Valid RequestLogin login) {
        try {

            var user = userRepository.getReferenceByEmail(login.email());

            if (!user.getPassword().equals(login.password())) {
                return ResponseEntity.badRequest().body("Credenciais inválidas.");
            }
            var token = user.generateToken();
            userRepository.save(user);
            return ResponseEntity.ok().body(new ResponseLogin(token, user.getProfile()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body("Credenciais inválidas.");
        }


    }
}
