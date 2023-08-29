package com.example.demo.builders.models;

import com.example.demo.enums.Profile;
import com.example.demo.models.User;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserBuilder {
    private UUID id;
    private final String name = "any";
    private String email = "any@email.com";
    private final String phone = "12345678999";
    private String password = "any_password";

    private Profile profile = Profile.CLIENT;


    private UserBuilder() {
    }

    public static UserBuilder init() {
        return new UserBuilder();
    }

    public UserBuilder withProfile(Profile profile) {
        this.profile = profile;
        return this;
    }

    public UserBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder withEmail(String email) {
        this.email = email;
        return this;
    }


    public User builder() {
        return new User(
                name,
                email,
                phone,
                password,
                profile
        );
    }


}
