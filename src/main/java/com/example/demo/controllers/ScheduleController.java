package com.example.demo.controllers;

import com.example.demo.dtos.*;
import com.example.demo.models.Schedule;
import com.example.demo.models.User;
import com.example.demo.repositories.AnimalRepository;
import com.example.demo.repositories.ScheduleRepository;
import com.example.demo.repositories.ServiceRepository;
import com.example.demo.repositories.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static java.time.DayOfWeek.SUNDAY;
@CrossOrigin(origins = "*")

@RestController
@RequestMapping("/schedules")
public class ScheduleController {
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @GetMapping
    public ResponseEntity getAllSchedules() {
        return ResponseEntity.ok().body(scheduleRepository.findAll().stream().map(s -> new ListSchedules(s.getId(), s.getDateHour(), new ListSchedulesUser(s.getUser()), s.getAnimal(), s.getService())).toList());

    }

    @GetMapping("/schedulesByUser")
    public ResponseEntity getSchedulesByUser(@AuthenticationPrincipal User userLogged) {
        var user = userRepository.findById(userLogged.getId());
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorData("user", "id inválido"));
        }


        var schedules = scheduleRepository.findByUserId(userLogged.getId());


        return ResponseEntity.ok().body(schedules.stream().map(s -> new ListSchedules(s.getId(), s.getDateHour(), new ListSchedulesUser(s.getUser()), s.getAnimal(), s.getService())).toList());

    }

    @GetMapping("/filterDate")
    public ResponseEntity filterDate(@RequestParam LocalDateTime date) {
        var schedules = scheduleRepository.findByDate(date.toLocalDate().toString());

        return ResponseEntity.ok().body(schedules.stream().map(s -> new ScheduleByDate(s.getDateHour())));

    }


    @PostMapping
    @Transactional
    public ResponseEntity createSchedule(@AuthenticationPrincipal User userLogged, @Valid @RequestBody CreateSchedule newSchedule) {

        var user = userRepository.findById(userLogged.getId());
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorData("user", "id inválido"));
        }

        var hour = newSchedule.dateHour().getHour();

        var day = newSchedule.dateHour().getDayOfWeek();
        if (hour < 9 || hour > 18 || day == SUNDAY)
            return ResponseEntity.badRequest().body(new ErrorData("schedule", "Data ou Horário inválidos!"));

        if (newSchedule.dateHour().getMinute() > 0 || newSchedule.dateHour().getSecond() > 0)
            return ResponseEntity.badRequest().body(new ErrorData("schedule", "Data ou Horário inválidos!"));

        if (scheduleRepository.existsByDateHour(newSchedule.dateHour()))
            return ResponseEntity.badRequest().body(new ErrorData("schedule", "Já existe agendamento nessa data e horário informado!"));

        var animal = animalRepository.getReferenceById(newSchedule.idAnimal());
        var service = serviceRepository.getReferenceById(newSchedule.idService());

        scheduleRepository.save(new Schedule(newSchedule.dateHour(), animal, user.get(), service));

        return ResponseEntity.ok().body("Serviço cadastrado");
    }
}
