package com.example.demo.controllers;

import com.example.demo.builders.dtos.CreateUserBuilder;
import com.example.demo.builders.models.UserBuilder;
import com.example.demo.config.HandleException;
import com.example.demo.dtos.UserList;
import com.example.demo.enums.Profile;
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

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = true)
class UserControllerTest {
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


    // Create CLIENT
    @Test
    @DisplayName("Deve retornar 400 quando cadastrar usuario do tipo client com email ja cadastrado")
    void createClientCase1() throws Exception {
        // given (dado)

        var user = CreateUserBuilder.init().builder();

        var dataJson = mapper.writeValueAsString(user);

        userRepository.save(
                UserBuilder.init().withEmail(user.email()).builder()
        );
        // when (quando)
        var response = mockMvc.perform(
                MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dataJson)
        ).andReturn().getResponse();

        // then (entao)
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        var data = mapper.readValue(response.getContentAsString(), HandleException.ErrorData.class);

        assertThat(data.field()).isEqualTo("user");
        assertThat(data.message()).isEqualTo("E-mail ja cadastrado");
    }

    @Test
    @DisplayName("Deve retornar 400 quando informar dados incorretos")
    void createClientCase2() throws Exception {
        // given (dado)
        var dataJson = mapper.writeValueAsString(CreateUserBuilder.init().withEmail("invalid_email").withPhone("1234").builder());

        // when (quando)
        var response = mockMvc.perform(
                MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dataJson)
        ).andReturn().getResponse();

        // then (entao)
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        var data = Arrays.asList(mapper.readValue(response.getContentAsString(), HandleException.ErrorData[].class));

        assertThat(data.size()).isEqualTo(2);

        assertThat(
                data.stream().anyMatch(error -> error.field().equalsIgnoreCase("email"))
        ).isTrue();

        assertThat(
                data.stream().anyMatch(error -> error.field().equalsIgnoreCase("phone"))
        ).isTrue();
    }

    @Test
    @DisplayName("Deve retornar 200 quando cadastrar usuario do tipo client")
    void createClientCase3() throws Exception {
        // given (dado)
        var dataJson = mapper.writeValueAsString(UserBuilder.init().withProfile(Profile.CLIENT).builder());

        // when (quando)
        var response = mockMvc.perform(
                MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dataJson)
        ).andReturn().getResponse();

        // then (entao)
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }


// Create ADMIN

    @Test
    @DisplayName("Deve retornar erro 403 quando usuario do tipo client tentar cadastrar admin")
    void createAdminCase1() throws Exception {
        // given (dado)
        // Crie uma instância de UserDetails que represente o usuário autenticado
        UserDetails userDetails = UserBuilder.init().withProfile(Profile.CLIENT).builder();

        // Crie uma instância de Authentication contendo os detalhes de autenticação
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

        // Configure o SecurityContextHolder para conter a autenticação
        SecurityContextHolder.getContext().setAuthentication(authentication);

        var dataJson = mapper.writeValueAsString(CreateUserBuilder.init().builder());

        // when (quando)
        var response = mockMvc.perform(
                MockMvcRequestBuilders.post("/users/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dataJson)
        ).andReturn().getResponse();

        // then (entao)
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }

    @Test
    @DisplayName("Deve retornar 400 quando cadastrar usuario do tipo admin com email ja cadastrado")
    void createAdminCase2() throws Exception {
        // given (dado)

        UserDetails userDetails = UserBuilder.init().withProfile(Profile.ADMIN).builder();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);


        var user = CreateUserBuilder.init().builder();

        var dataJson = mapper.writeValueAsString(user);

        userRepository.save(
                UserBuilder.init().withEmail(user.email()).withProfile(Profile.ADMIN).builder()
        );

        // when (quando)
        var response = mockMvc.perform(
                MockMvcRequestBuilders.post("/users/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dataJson)
        ).andReturn().getResponse();

        // then (entao)
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        var data = mapper.readValue(response.getContentAsString(), HandleException.ErrorData.class);

        assertThat(data.field()).isEqualTo("user");
        assertThat(data.message()).isEqualTo("E-mail ja cadastrado");
    }

    @Test
    @DisplayName("Deve retornar 400 quando informar dados incorretos")
    void createAdminCase3() throws Exception {
        // given (dado)
        UserDetails userDetails = UserBuilder.init().withProfile(Profile.ADMIN).builder();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);


        var dataJson = mapper.writeValueAsString(CreateUserBuilder.init().withEmail("invalid_email").withPhone("1234").builder());

        // when (quando)
        var response = mockMvc.perform(
                MockMvcRequestBuilders.post("/users/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dataJson)
        ).andReturn().getResponse();

        // then (entao)
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        var data = Arrays.asList(mapper.readValue(response.getContentAsString(), HandleException.ErrorData[].class));

        assertThat(data.size()).isEqualTo(2);

        assertThat(
                data.stream().anyMatch(error -> error.field().equalsIgnoreCase("email"))
        ).isTrue();

        assertThat(
                data.stream().anyMatch(error -> error.field().equalsIgnoreCase("phone"))
        ).isTrue();
    }

    @Test
    @DisplayName("Deve retornar 200 quando cadastrar usuario do tipo admin")
    void createAdminCase4() throws Exception {
        // given (dado)
        UserDetails userDetails = UserBuilder.init().withProfile(Profile.ADMIN).builder();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);


        var dataJson = mapper.writeValueAsString(UserBuilder.init().withProfile(Profile.ADMIN).builder());

        // when (quando)
        var response = mockMvc.perform(
                MockMvcRequestBuilders.post("/users/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dataJson)
        ).andReturn().getResponse();

        // then (entao)
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }


    @Test
    @DisplayName("Deve retornar 404 quando nao encontrar o usuario")
    void getUserCase1() throws Exception {
//        given
        UserDetails userDetails = UserBuilder.init().builder();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

//        when
        var response = mockMvc.perform(
                MockMvcRequestBuilders.get("/users/getUser")
        ).andReturn().getResponse();
//        then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("Deve retornar 200 quando encontrar o usuario")
    void getUserCase2() throws Exception {
//        given
        var user = UserBuilder.init().builder();
        userRepository.save(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, ((UserDetails) user).getPassword(), ((UserDetails) user).getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

//        when
        var response = mockMvc.perform(
                MockMvcRequestBuilders.get("/users/getUser")
        ).andReturn().getResponse();
//        then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Deve retornar 200, contendo dois usuarios na lista")
    void listCase1() throws Exception {
        UserDetails userDetails = UserBuilder.init().builder();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        var user1 = UserBuilder.init().builder();
        var user2 = UserBuilder.init().withEmail("any2@email.com").builder();

        userRepository.save(user1);
        userRepository.save(user2);

        var response = mockMvc.perform(MockMvcRequestBuilders.get("/users")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        var contentData = Arrays.asList(mapper.readValue(response.getContentAsString(), UserList[].class));

        assertThat(contentData.size()).isEqualTo(2);

    }

}
