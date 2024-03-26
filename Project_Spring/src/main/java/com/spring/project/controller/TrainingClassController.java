package com.spring.project.controller;

import com.spring.project.dto.TrainingClassRequest;
import com.spring.project.dto.TrainingClassResponse;
import com.spring.project.model.TrainingClass;
import com.spring.project.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(path = "project/api/v1/classes")
@AllArgsConstructor
public class TrainingClassController {

    @Autowired
    private final AdminService adminService;

    @GetMapping
    public ResponseEntity<List<TrainingClassResponse>> getTrainingClasses(){
        List<TrainingClassResponse> trainingClassResponse = adminService.getAllTrainingClasses();
        return ResponseEntity.ok(trainingClassResponse);
    }

    @GetMapping("/{trainingClassId}")
    public ResponseEntity<TrainingClassResponse> getTrainingClass(@PathVariable("trainingClassId") Long trainingClassId){
        TrainingClassResponse trainingClassResponse = adminService.getTrainingClass(trainingClassId);
        return ResponseEntity.ok(trainingClassResponse);
    }

    @DeleteMapping("/{trainingClassId}")
    public ResponseEntity<String> deleteTrainingClass(@PathVariable("trainingClassId") Long trainingClassId){
        adminService.deleteTrainingClass(trainingClassId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping
    public ResponseEntity<TrainingClass> createTrainingClass(@RequestBody TrainingClassRequest classRequest) {
        TrainingClass trainingClass = adminService.createTrainingClass(classRequest);
        return ResponseEntity.ok(trainingClass);
    }

    @PutMapping("/{trainingClassId}")
    public ResponseEntity<Void> updateTrainingClass(@PathVariable("trainingClassId") Long trainingClassId, @RequestBody TrainingClassRequest trainingClassRequest){
        adminService.updateTrainingClass(trainingClassId,trainingClassRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
