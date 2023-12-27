package com.spring.project.controller;

import com.spring.project.model.TrainingClass;
import com.spring.project.service.TrainerService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(path = "project/api/trainer")
@AllArgsConstructor
public class TrainerController {

    @Autowired
    private final TrainerService trainerService;
    @GetMapping
    public ResponseEntity<String> TrainerPage(){
        return ResponseEntity.ok("Ai accesat pagina trainerului");
    }

    @GetMapping("/classes")
    public ResponseEntity<List<TrainingClass>> getTrainingClasses(){
        List<TrainingClass> trainingClassesForTrainer = trainerService.getTrainingClassesForTrainer();
        return ResponseEntity.ok(trainingClassesForTrainer);
    }
}
