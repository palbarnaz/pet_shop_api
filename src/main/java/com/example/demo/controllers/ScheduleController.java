package com.example.demo.controllers;

import com.example.demo.dtos.CreateSchedule;
import com.example.demo.dtos.ErrorData;
import com.example.demo.models.Schedule;
import com.example.demo.repositories.AnimalRepository;
import com.example.demo.repositories.ScheduleRepository;
import com.example.demo.repositories.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static java.time.DayOfWeek.SUNDAY;

@RestController
@RequestMapping("/schedules")
public class ScheduleController {
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/{idUser}")
    @Transactional
    public ResponseEntity createSchedule(@Valid @RequestBody CreateSchedule newSchedule, @RequestHeader("AuthToken") String token, @PathVariable UUID idUser) {

        var user = userRepository.findById(idUser);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorData("user", "id inválido"));
        }


        if (!user.get().isAuthenticated(token)) {
            return ResponseEntity.badRequest().body(new ErrorData("token", "token inválido"));
        }


        var hour = newSchedule.dateHour().getHour();
        var day = newSchedule.dateHour().getDayOfWeek();
        if (hour < 9 || hour > 18 || day == SUNDAY)
            return ResponseEntity.badRequest().body("Data ou Horário inválidos!");


        if (newSchedule.dateHour().getMinute() > 0 || newSchedule.dateHour().getSecond() > 0)
            return ResponseEntity.badRequest().body("Horário errado");

        if (scheduleRepository.existsByDateHour(newSchedule.dateHour()))
            return ResponseEntity.badRequest().body("Já existe agendamento nessa data e horário informado!");


        var schedule = scheduleRepository.save(new Schedule(newSchedule));
        return ResponseEntity.ok().body(schedule);
    }
}
