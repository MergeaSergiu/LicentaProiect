package com.spring.project.controller;

import com.spring.project.model.TrainingClass;
import com.spring.project.service.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping(path = "project/api/user")
public class ClientClassController {

    @Autowired
    private final ClientService clientService;

    @GetMapping("/classes")
    public ResponseEntity<List<TrainingClass>> getTrainingClasses(){
        List<TrainingClass> trainingClasses = clientService.getTrainingClasses();
        return ResponseEntity.ok(trainingClasses);
    }

    @PostMapping("/enrollUser")
    public ResponseEntity<String> enrollUserToTrainingClass(@RequestParam("className") String className){
        clientService.enrollUserToTrainingClass(className);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/getEnrollClasses")
    public ResponseEntity<List<String>> getEnrollClassesForUser(){
        return ResponseEntity.ok(clientService.getEnrollClasses());
    }


}
