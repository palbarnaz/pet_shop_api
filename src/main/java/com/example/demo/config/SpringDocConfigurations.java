package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfigurations {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Pet Shop")
                        .description("API para realizar agendamentos dos serviços do Pet Shop")
                        .contact(new Contact()
                                .name("Paola Albarnaz")
                                .email("paola.silva.nh1@gmail.com")));
    }
}

