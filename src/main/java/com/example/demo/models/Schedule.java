package com.example.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "schedules")
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "date_hour")
    private LocalDateTime dateHour;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_client")
    private User user;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_animal")

    private Animal animal;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_service")

    private Service service;


    public Schedule(LocalDateTime dateHour, Animal animal, User user, Service service) {

        this.dateHour = dateHour;
        this.user = user;
        this.animal = animal;
        this.service = service;


    }
}
