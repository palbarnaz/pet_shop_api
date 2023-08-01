package com.example.demo.controllers;

import com.example.demo.dtos.CreateService;
import com.example.demo.dtos.EditService;
import com.example.demo.dtos.ErrorData;
import com.example.demo.dtos.ResponseService;
import com.example.demo.models.Service;
import com.example.demo.repositories.ServiceRepository;
import com.example.demo.repositories.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @PostMapping("/{idUser}")
    @Transactional
    public ResponseEntity createService(@RequestBody @Valid CreateService newService, @RequestHeader("AuthToken") String token, @PathVariable UUID idUser) {

        var user = userRepository.findById(idUser);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorData("user", "id inválido"));
        }
        if (!user.get().isAuthenticated(token)) {
            return ResponseEntity.badRequest().body(new ErrorData("token", "token inválido"));
        }

        if (serviceRepository.findByDescription(newService.description()).isPresent()) {
            return ResponseEntity.badRequest().body("Este serviço já foi criado! ");
        }

        var service = new Service(newService, idUser);

        serviceRepository.save(service);

        return ResponseEntity.ok(new ResponseService(service.getDescription(), service.getDuration(), service.getPrice()));
    }

    @PutMapping("/{idService}/{idUser}")
    @Transactional
    public ResponseEntity editService(@RequestBody @Valid EditService newService, @PathVariable UUID idService, @PathVariable UUID idUser, @RequestHeader("AuthToken") String token) {
        var user = userRepository.findById(idUser);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorData("user", "id inválido"));
        }

        if (!user.get().isAuthenticated(token)) {
            return ResponseEntity.badRequest().body(new ErrorData("token", "token inválido"));
        }

        var service = serviceRepository.findById(idService);

        if (service.isEmpty()) return ResponseEntity.badRequest().body("Serviço não encontrado!");

        var editService = service.get();

        editService.update(newService, idUser);


        return ResponseEntity.ok().body(new ResponseService(editService.getDescription(), editService.getDuration(), editService.getPrice()));
    }

    @DeleteMapping("/{idService}/{idUser}")
    @Transactional
    public ResponseEntity deleteService(@Valid @PathVariable UUID idService, @RequestHeader("AuthToken") String token, @PathVariable UUID idUser) {

        var user = userRepository.findById(idUser);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorData("user", "id inválido"));
        }

        if (!user.get().isAuthenticated(token)) {
            return ResponseEntity.badRequest().body(new ErrorData("token", "token inválido"));
        }

        if (!serviceRepository.existsById(idService))
            return ResponseEntity.badRequest().body("Serviço não encontrado!");


        serviceRepository.deleteById(idService);

        return ResponseEntity.ok().body("Serviço deletado!");
    }


}
