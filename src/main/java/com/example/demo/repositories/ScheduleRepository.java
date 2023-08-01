package com.example.demo.repositories;

import com.example.demo.models.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {
    boolean existsByDateHour(LocalDateTime dataHour);


}
