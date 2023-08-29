package com.example.demo.controllers;

import com.example.demo.builders.dtos.CreateScheduleBuilder;
import com.example.demo.builders.models.AnimalBuilder;
import com.example.demo.builders.models.ScheduleBuilder;
import com.example.demo.builders.models.ServiceBuilder;
import com.example.demo.builders.models.UserBuilder;
import com.example.demo.dtos.ListSchedules;
import com.example.demo.enums.Profile;
import com.example.demo.models.Schedule;
import com.example.demo.models.User;
import com.example.demo.repositories.AnimalRepository;
import com.example.demo.repositories.ScheduleRepository;
import com.example.demo.repositories.ServiceRepository;
import com.example.demo.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = true)
class ScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private AnimalRepository animalRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @AfterEach
    public void afterEach() {
        scheduleRepository.deleteAll();
        serviceRepository.deleteAll();
        animalRepository.deleteAll();
        userRepository.deleteAll();
    }

    public Schedule createSchedule(LocalDateTime date, User user) {
        var userAdmin = UserBuilder.init()
                .withEmail("admin@gmail.com")
                .withProfile(Profile.ADMIN)
                .builder();

        userRepository.save(userAdmin);

        var service = ServiceBuilder.init().withIdAdmin(userAdmin.getId()).builder();
        serviceRepository.save(service);

        var animal = AnimalBuilder.init().withIdUser(user.getId()).builder();
        animalRepository.save(animal);

        return ScheduleBuilder.init()
                .withUser(user)
                .withAnimal(animal)
                .withService(service)
                .withDateHour(LocalDateTime.parse(date.toString())
                ).builder();


    }

    @Test
    @DisplayName("Deve retornar erro 404 quando nao encontrar um usuario")
    void createScheduleCase1() throws Exception {
        UserDetails userDetails = UserBuilder.init().builder();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        var dataJson = mapper.writeValueAsString(CreateScheduleBuilder.init().builder());
//        when
        var response = mockMvc.perform(
                MockMvcRequestBuilders.post("/schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dataJson)
        ).andReturn().getResponse();
//        then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando informar horario mais cedo do que o horario de funcionamento do pet shop.")
    void createScheduleCase2() throws Exception {
        var user = UserBuilder.init().builder();
        userRepository.save(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, ((UserDetails) user).getPassword(), ((UserDetails) user).getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);


        var dataJson = mapper.writeValueAsString(CreateScheduleBuilder.init().withDateHour(LocalDateTime.parse("2023-09-22T08:00:00")).builder());
//        when
        var response = mockMvc.perform(
                MockMvcRequestBuilders.post("/schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dataJson)
        ).andReturn().getResponse();
//        then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando informar horario mais tarde do que o horario de funcionamento do pet shop.")
    void createScheduleCase3() throws Exception {
        var user = UserBuilder.init().builder();
        userRepository.save(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, ((UserDetails) user).getPassword(), ((UserDetails) user).getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);


        var dataJson = mapper.writeValueAsString(CreateScheduleBuilder.init().withDateHour(LocalDateTime.parse("2023-09-22T19:00:00")).builder());
//        when
        var response = mockMvc.perform(
                MockMvcRequestBuilders.post("/schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dataJson)
        ).andReturn().getResponse();
//        then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando informar dia fora do funcionamento do pet shop.")
    void createScheduleCase4() throws Exception {
        var user = UserBuilder.init().builder();
        userRepository.save(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, ((UserDetails) user).getPassword(), ((UserDetails) user).getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        var dataJson = mapper.writeValueAsString(CreateScheduleBuilder.init().withDateHour(LocalDateTime.parse("2023-09-24T15:00:00")).builder());
//        when
        var response = mockMvc.perform(
                MockMvcRequestBuilders.post("/schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dataJson)
        ).andReturn().getResponse();
//        then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando informar minutos no horario do agendamento")
    void createScheduleCase5() throws Exception {
        var user = UserBuilder.init().builder();
        userRepository.save(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, ((UserDetails) user).getPassword(), ((UserDetails) user).getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);


        var dataJson = mapper.writeValueAsString(CreateScheduleBuilder.init().withDateHour(LocalDateTime.parse("2023-09-22T15:30:00")).builder());
//        when
        var response = mockMvc.perform(
                MockMvcRequestBuilders.post("/schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dataJson)
        ).andReturn().getResponse();
//        then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando informar segundos no horario do agendamento")
    void createScheduleCase6() throws Exception {
        var user = UserBuilder.init().builder();
        userRepository.save(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, ((UserDetails) user).getPassword(), ((UserDetails) user).getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);


        var dataJson = mapper.writeValueAsString(CreateScheduleBuilder.init().withDateHour(LocalDateTime.parse("2023-09-22T15:00:01")).builder());
//        when
        var response = mockMvc.perform(
                MockMvcRequestBuilders.post("/schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dataJson)
        ).andReturn().getResponse();
//        then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando informar agendamento que já existe no banco de dados ")
    void createScheduleCase7() throws Exception {
        var user = UserBuilder.init().builder();
        userRepository.save(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, ((UserDetails) user).getPassword(), ((UserDetails) user).getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        var dataSchedule = createSchedule(LocalDateTime.parse("2023-09-22T15:00:00"), user);
        scheduleRepository.save(dataSchedule);

        var dataJson = mapper.writeValueAsString(CreateScheduleBuilder.init().withIdAnimal(dataSchedule.getAnimal().getId()).withIdService(dataSchedule.getService().getId()).withDateHour(dataSchedule.getDateHour()).builder());
//        when
        var response = mockMvc.perform(
                MockMvcRequestBuilders.post("/schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dataJson)
        ).andReturn().getResponse();
//        then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deve retornar 200 quando criar agendamento ")
    void createScheduleCase8() throws Exception {
        var user = UserBuilder.init().builder();
        userRepository.save(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, ((UserDetails) user).getPassword(), ((UserDetails) user).getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);


        var dataSchedule = createSchedule(LocalDateTime.parse("2023-09-08T15:00:00"), user);

        var dataJson = mapper.writeValueAsString(
                CreateScheduleBuilder.init()
                        .withIdAnimal(dataSchedule.getAnimal()
                                .getId()).withIdService(dataSchedule.getService().getId())
                        .withDateHour(dataSchedule.getDateHour()
                        ).builder());
//        when
        var response = mockMvc.perform(
                MockMvcRequestBuilders.post("/schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dataJson)
        ).andReturn().getResponse();
//        then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Deve retornar 200 com uma lista vazia quando nao existir nenhum horario ocupado")
    void filterDateCase1() throws Exception {
        var user = UserBuilder.init().builder();
        userRepository.save(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, ((UserDetails) user).getPassword(), ((UserDetails) user).getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);


        var response = mockMvc.perform(MockMvcRequestBuilders.get("/schedules/filterDate")
                .queryParam("date", LocalDateTime.now().plusHours(1).toString())
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("[]");


    }

    @Test
    @DisplayName("Deve retornar 200 com uma lista contendo informacoes de um agendamento")
    void filterDateCase2() throws Exception {
        var user = UserBuilder.init().builder();
        userRepository.save(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, ((UserDetails) user).getPassword(), ((UserDetails) user).getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);


        var dataSchedule = createSchedule(LocalDateTime.parse("2023-09-22T15:00:00"), user);
        scheduleRepository.save(dataSchedule);


        var response = mockMvc.perform(MockMvcRequestBuilders.get("/schedules/filterDate")
                .queryParam("date", "2023-09-22T15:00:00")
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        var contentDate = Arrays.asList(
                mapper.readValue(response.getContentAsString(), ListSchedules[].class)
        );

        assertThat(contentDate.size()).isEqualTo(1);


    }

    @Test
    @DisplayName("Deve retornar erro 404 quando nao encontrar o usuario")
    void getSchedulesByUserCase1() throws Exception {
        UserDetails userDetails = UserBuilder.init().builder();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        var response = mockMvc.perform(MockMvcRequestBuilders.get("/schedules/schedulesByUser")
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("Deve retornar 200 e uma lista vazia quando o usuario nao tiver agendamento")
    void getSchedulesByUserCase2() throws Exception {
        var user = UserBuilder.init().builder();
        userRepository.save(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, ((UserDetails) user).getPassword(), ((UserDetails) user).getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        var response = mockMvc.perform(MockMvcRequestBuilders.get("/schedules/schedulesByUser")
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("[]");

    }

    @Test
    @DisplayName("Deve retornar 200 e uma lista com um agendamento")
    void getSchedulesByUserCase3() throws Exception {
        var user = UserBuilder.init().builder();
        userRepository.save(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, ((UserDetails) user).getPassword(), ((UserDetails) user).getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        var dataSchedule = createSchedule(LocalDateTime.parse("2023-09-22T15:00:00"), user);
        scheduleRepository.save(dataSchedule);


        var response = mockMvc.perform(MockMvcRequestBuilders.get("/schedules/schedulesByUser")
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        var contentDate = Arrays.asList(
                mapper.readValue(response.getContentAsString(), ListSchedules[].class)
        );

        assertThat(contentDate.size()).isEqualTo(1);

    }

}