package com.spring.project.controller;

import com.spring.project.dto.TrainingClassResponse;
import com.spring.project.model.TrainingClass;
import com.spring.project.service.impl.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping(path = "project/api/user")
public class ClientClassController {

    @Autowired
    private final ClientService clientService;

    @GetMapping("/classes")
    public ResponseEntity<List<TrainingClassResponse>> getTrainingClasses(){
        List<TrainingClassResponse> trainingClasses = clientService.getTrainingClasses();
        return ResponseEntity.ok(trainingClasses);
    }

    @PostMapping("/enrollUser")
    public ResponseEntity<?> enrollUserToTrainingClass(@RequestParam("className") String className){
        clientService.enrollUserToTrainingClass(className);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/unenroll")
    public ResponseEntity<String> UnenrollUserFromTrainingClass(@RequestParam("className") String className){
        clientService.unEnrollUserFromTrainingClass(className);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Enrollment was deleted");
    }
}
