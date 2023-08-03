package com.example.demo.repositories;

import com.example.demo.models.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {
    boolean existsByDateHour(LocalDateTime dataHour);

    List<Schedule> findByUserId(UUID idUser);

    @Query(value = "SELECT * FROM schedules s WHERE TO_CHAR(s.date_hour, 'YYYY-MM-DD') LIKE %:date%", nativeQuery = true)
    List<Schedule> findByDate(String date);

}
