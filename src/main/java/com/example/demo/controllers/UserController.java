package com.example.demo.controllers;

import com.example.demo.dtos.CreateUser;
import com.example.demo.dtos.ErrorData;
import com.example.demo.dtos.ResponseUser;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*")

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;


    @GetMapping
    public ResponseEntity<List<User>> list() {
        var users = userRepository.findAll();
        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/{number}")
    public ResponseEntity getUser(@PathVariable UUID number, @RequestHeader("AuthToken") String token) {
        var user = userRepository.getReferenceById(number);

        if (!user.isAuthenticated(token)) {
            return ResponseEntity.badRequest().body(new ErrorData("token", "Token Inválido"));
        }

        return ResponseEntity.ok().body(new ResponseUser(user));
    }

    @PostMapping
    @Transactional
    public ResponseEntity createClient(@RequestBody @Valid CreateUser newUser) {

//        if (newUser.profile().equals(Profile.ADMIN)) {
//            return ResponseEntity.badRequest().body("Somente administradores podem criar esse perfil!");
//        }

        if (userRepository.existsByEmail(newUser.email())) {
            return ResponseEntity.badRequest().body(new ErrorData("user", "E-mail já cadastrado!"));
        }


        var user = new User(newUser);

        userRepository.save(user);

        return  ResponseEntity.ok(new ResponseUser(user));

    }
}
