package com.example.demo.dtos;

public record ResponseLoginClient(String tokenJwt, ResponseClient user) {
}
