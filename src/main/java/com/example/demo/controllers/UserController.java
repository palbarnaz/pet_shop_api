package com.example.demo.controllers;

import com.example.demo.config.HandleException;
import com.example.demo.dtos.CreateUser;
import com.example.demo.dtos.ResponseAdmin;
import com.example.demo.dtos.ResponseClient;
import com.example.demo.enums.Profile;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<List<User>> list() {
        var users = userRepository.findAll();
        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/getUser")
    public ResponseEntity getUser(@AuthenticationPrincipal User userLogged) {

        if (userLogged.getId() == null) {
            return ResponseEntity.notFound().build();
        }

        var user = userRepository.findById(userLogged.getId()).get();
        var outputUser = new ResponseClient(user);
        return ResponseEntity.ok().body(outputUser);
    }

    @PostMapping
    @Transactional
    public ResponseEntity createClient(@RequestBody @Valid CreateUser newUser) {

        if (userRepository.existsByEmail(newUser.email())) {
            return ResponseEntity.badRequest().body(new HandleException.ErrorData("user", "E-mail ja cadastrado"));
        }


        var user = new User(
                newUser.name(),
                newUser.email(),
                newUser.phone(),
                passwordEncoder.encode(newUser.password()),
                Profile.CLIENT
        );

        userRepository.save(user);

        return ResponseEntity.ok(new ResponseClient(user));

    }


    @PostMapping("/admin")
    @Transactional
    public ResponseEntity createAdmin(@AuthenticationPrincipal User userLogged, @RequestBody @Valid CreateUser newUser) {

        if (userLogged.getProfile() == Profile.CLIENT)
            return ResponseEntity.badRequest().body(new HandleException.ErrorData("user", "Somente administradores podem cadastrar outros administradores!"));


        if (userRepository.existsByEmail(newUser.email())) {
            return ResponseEntity.badRequest().body(new HandleException.ErrorData("user", "E-mail ja cadastrado"));
        }


        var user = new User(
                newUser.name(),
                newUser.email(),
                newUser.phone(),
                passwordEncoder.encode(newUser.password()),
                Profile.ADMIN
        );

        userRepository.save(user);

        return ResponseEntity.ok(new ResponseAdmin(user));

    }
}
