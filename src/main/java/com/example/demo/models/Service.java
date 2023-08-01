package com.example.demo.models;

import com.example.demo.dtos.CreateService;
import com.example.demo.dtos.EditService;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "services")
@AllArgsConstructor
@NoArgsConstructor
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String description;
    private int duration;
    private double price;

    @Column(name = "id_admin")
    private UUID idAdmin;


    public Service(CreateService newService, UUID idAdmin) {
        this.description = newService.description();
        this.duration = newService.duration();
        this.price = newService.price();
        this.idAdmin = idAdmin;
    }

    public void update(EditService s, UUID idAdmin) {
        description = s.description();
        duration = s.duration();
        price = s.price();
        this.idAdmin = idAdmin;
    }
}
