package com.example.demo.controllers;

import com.example.demo.dtos.CreateService;
import com.example.demo.dtos.EditService;
import com.example.demo.dtos.ErrorData;
import com.example.demo.dtos.ResponseService;
import com.example.demo.enums.Profile;
import com.example.demo.models.Service;
import com.example.demo.models.User;
import com.example.demo.repositories.ServiceRepository;
import com.example.demo.repositories.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin(origins = "*")

@RestController
@RequestMapping("/services")
public class ServiceController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ServiceRepository serviceRepository;

    @GetMapping
    public ResponseEntity getServices() {
        var services = serviceRepository.findAll();
        return ResponseEntity.ok().body(services);
    }

    @PostMapping()
    @Transactional
    public ResponseEntity createService(@AuthenticationPrincipal User userLogged, @RequestBody @Valid CreateService newService) {

        var user = userRepository.findById(userLogged.getId());
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorData("user", "id inválido"));
        }

        if (user.get().getProfile() == Profile.CLIENT)
            return ResponseEntity.badRequest().body(new ErrorData("serviço", "Cliente não pode cadastrar serviço!"));



        if (serviceRepository.findByDescription(newService.description()).isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorData("service", "Este serviço já foi criado! "));
        }

        var service = new Service(newService, userLogged.getId());

        serviceRepository.save(service);

        return ResponseEntity.ok(new ResponseService(service.getDescription(), service.getDuration(), service.getPrice()));
    }

    @PutMapping("/{idService}")
    @Transactional
    public ResponseEntity editService(@AuthenticationPrincipal User userLogged, @RequestBody @Valid EditService newService, @PathVariable UUID idService) {
        var user = userRepository.findById(userLogged.getId());
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorData("user", "id inválido"));
        }

        if (user.get().getProfile() == Profile.CLIENT)
            return ResponseEntity.badRequest().body(new ErrorData("serviço", "Cliente não pode editar serviço!"));


        var service = serviceRepository.findById(idService);

        if (service.isEmpty())
            return ResponseEntity.badRequest().body(new ErrorData("service", "Serviço não encontrado!"));

        var editService = service.get();

        editService.update(newService, userLogged.getId());


        return ResponseEntity.ok().body(new ResponseService(editService.getDescription(), editService.getDuration(), editService.getPrice()));
    }

    @DeleteMapping("/{idService}")
    @Transactional
    public ResponseEntity deleteService(@Valid @PathVariable UUID idService, @AuthenticationPrincipal User userLogged) {

        var user = userRepository.findById(userLogged.getId());
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorData("user", "id inválido"));
        }

        if (user.get().getProfile() == Profile.CLIENT)
            return ResponseEntity.badRequest().body(new ErrorData("serviço", "Cliente não pode deletar serviço!"));


        if (!serviceRepository.existsById(idService))
            return ResponseEntity.badRequest().body(new ErrorData("service", "Serviço não encontrado!"));


        serviceRepository.deleteById(idService);

        return ResponseEntity.ok().body("Serviço deletado!");
    }


}
