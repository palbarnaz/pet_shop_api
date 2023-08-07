package com.example.demo.controllers;

import com.example.demo.dtos.ErrorData;
import com.example.demo.dtos.RequestLogin;
import com.example.demo.dtos.ResponseUser;
import com.example.demo.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@CrossOrigin(origins = "*")

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
                return ResponseEntity.badRequest().body(new ErrorData("login", "Credenciais inválidas."));
            }
            var token = user.generateToken();
            userRepository.save(user);
            return ResponseEntity.ok().body(new ResponseUser(user));
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body(new ErrorData("login", "Credenciais inválidas."));
        }


    }
}
