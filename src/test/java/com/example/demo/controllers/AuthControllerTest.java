package com.example.demo.controllers;

import com.example.demo.builders.dtos.CreateUserBuilder;
import com.example.demo.builders.models.UserBuilder;
import com.example.demo.dtos.RequestLogin;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc()
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @AfterEach
    public void afterEach() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve retornar erro 403 quando o email estiver incorreto")
    void doLoginCase1() throws Exception {
        var userDto = CreateUserBuilder.init().builder();

        var dataJson = mapper.writeValueAsString(
                new RequestLogin(
                        "invalid@email.com",
                        userDto.password()
                )
        );

        userRepository.save(
                UserBuilder.init().withPassword(passwordEncoder.encode(userDto.password())).builder()
        );

        var response = mockMvc.perform(
                MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dataJson)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());

    }

    @Test
    @DisplayName("Deve retornar erro 403 quando a senha estiver incorreta")
    void doLoginCase2() throws Exception {
        var userDto = CreateUserBuilder.init().builder();

        var dataJson = mapper.writeValueAsString(
                new RequestLogin(
                        userDto.email(),
                        "invalid_password"
                )
        );

        userRepository.save(
                UserBuilder.init().withPassword(passwordEncoder.encode(userDto.password())).builder()
        );

        var response = mockMvc.perform(
                MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dataJson)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @DisplayName("Deve retornar sucesso 200 quando as informacoes estiverem corretas.")
    void doLoginCase3() throws Exception {
        var userDto = CreateUserBuilder.init().builder();

        var dataJson = mapper.writeValueAsString(
                new RequestLogin(
                        userDto.email(),
                        userDto.password()
                )
        );

        userRepository.save(
                UserBuilder.init().withPassword(passwordEncoder.encode(userDto.password())).builder()
        );

        var response = mockMvc.perform(
                MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dataJson)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }


}