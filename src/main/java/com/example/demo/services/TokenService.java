package com.example.demo.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.demo.models.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    private final String secret = "Petshop@2023";

    public String getToken(User user) {
        try {
            var algorithm = Algorithm.HMAC256(secret);
            var expiresDate = LocalDateTime.now().plusHours(10).toInstant(ZoneOffset.of("-03:00"));
            return JWT.create()
                    .withIssuer("API Pet Shop")
                    .withSubject(user.getEmail())
                    .withClaim("id", user.getId().toString())
                    .withExpiresAt(expiresDate)
                    .sign(algorithm);
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Erro ao gerar token");
        }

    }

    public String verifyToken(String token) {
        var algorithm = Algorithm.HMAC256(secret);

        return JWT.require(algorithm)
                .withIssuer("API Pet Shop")
                .build()
                .verify(token)
                .getSubject();

    }
}
