package com.example.demo.dtos;

public record ResponseLogin(String tokenJwt, ResponseClient user) {
}
