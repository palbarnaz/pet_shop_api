package com.example.demo.models;

import com.example.demo.dtos.CreateSchedule;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
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
    @Column(name = "id_client")
    private UUID idClient;
    @Column(name = "id_animal")
    private UUID idAnimal;
    @Column(name = "id_service")
    private UUID idService;


    public Schedule(CreateSchedule newSchedule) {

        dateHour = newSchedule.dateHour();
        idClient = newSchedule.idClient();
        idAnimal = newSchedule.idAnimal();
        idService = newSchedule.idService();


    }
}
