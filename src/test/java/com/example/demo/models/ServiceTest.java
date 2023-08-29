package com.example.demo.models;

import com.example.demo.builders.dtos.UpdateServiceBuilder;
import com.example.demo.builders.models.ServiceBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ServiceTest {

    @Test
    @DisplayName("Deve atualizar dados do servico")
    void updateCase1() {
        //given (dado)

        var service = ServiceBuilder.init().builder();
        var serviceUpdate = UpdateServiceBuilder.init().builder();
        UUID idAdminUpdate = UUID.randomUUID();
        //when (quando)
        service.update(serviceUpdate, idAdminUpdate);
        //then (entao)
        assertThat(service.getDescription()).isEqualTo(serviceUpdate.description());
        assertThat(service.getDuration()).isEqualTo(serviceUpdate.duration());
        assertThat(service.getPrice()).isEqualTo(serviceUpdate.price());

    }
}