package com.example.demo.models;

import com.example.demo.dtos.CreateUser;
import com.example.demo.enums.Profile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@Table(name="users")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String email;
    private String phone;
    private String password;
    @Enumerated(EnumType.STRING)
    private Profile profile;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private List<Animal> animals;

    public User(CreateUser user) {
        this.name = user.name();
        this.email = user.email();
        this.phone = user.phone();
        this.password = user.password();
        this.profile = user.profile();
        animals = new ArrayList<>();
    }
}
