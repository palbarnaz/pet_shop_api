package com.example.demo.repositories;

import com.example.demo.models.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ServiceRepository extends JpaRepository<Service, UUID> {
    Optional<Service> findByDescription(String Description);
}
