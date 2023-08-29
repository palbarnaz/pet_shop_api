package com.example.demo.controllers;
import com.example.demo.dtos.*;
import com.example.demo.enums.Profile;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")

@RestController
@RequestMapping("/login")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;


    @PostMapping
    public ResponseEntity doLogin(@RequestBody @Valid RequestLogin data) {
        var token = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var authentication = manager.authenticate(token);
        var jwt = tokenService.getToken((User) authentication.getPrincipal());

        var user = userRepository.getReferenceByEmail(data.email());

        if (((User) authentication.getPrincipal()).getProfile().equals(Profile.ADMIN))
            return ResponseEntity.ok().body(new ResponseLoginAdmin(jwt, new ResponseAdmin(user)));


        return ResponseEntity.ok().body(new ResponseLoginClient(jwt, new ResponseClient(user)));
    }

}
