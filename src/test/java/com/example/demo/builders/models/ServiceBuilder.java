package com.example.demo.builders.models;

import com.example.demo.models.Service;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public class ServiceBuilder {
    private UUID id;
    private String description = "Consulta Veterinaria";
    private final int duration = 1;
    private double price = 50;

    private UUID idAdmin;

    private ServiceBuilder() {
    }

    public static ServiceBuilder init() {
        return new ServiceBuilder();
    }

    public ServiceBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public ServiceBuilder withPrice(double price) {
        this.price = price;
        return this;
    }

    public ServiceBuilder withIdAdmin(UUID idAdmin) {
        this.idAdmin = idAdmin;
        return this;
    }

    public Service builder() {
        return new Service(
                null,
                description,
                duration,
                price,
                idAdmin
        );
    }


}
