package com.example.demo.builders.dtos;

import com.example.demo.dtos.CreateUser;

import java.util.UUID;

public class CreateUserBuilder {
    private UUID id;
    private final String name = "any";

    private String email = "any@email.com";
    private String phone = "12345678999";
    private String password = "any_password";


    private CreateUserBuilder() {
    }

    public static CreateUserBuilder init() {
        return new CreateUserBuilder();
    }

    public CreateUserBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public CreateUserBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public CreateUserBuilder withPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public CreateUser builder() {
        return new CreateUser(
                name,
                email,
                phone,
                password
        );
    }

}
