package com.spring.project.controller;

import com.spring.project.dto.TrainingClassRequest;
import com.spring.project.dto.TrainingClassResponse;
import com.spring.project.model.TrainingClass;
import com.spring.project.service.AdminService;
import com.spring.project.service.TrainingClassService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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

    @Autowired
    private final TrainingClassService trainingClassService;

    @GetMapping
    public ResponseEntity<List<TrainingClassResponse>> getTrainingClasses(){
        List<TrainingClassResponse> trainingClassResponse = trainingClassService.getTrainingClasses();
        return new ResponseEntity<>(trainingClassResponse, HttpStatus.OK);
    }

    @GetMapping("/{trainingClassId}")
    public ResponseEntity<TrainingClassResponse> getTrainingClass(@PathVariable("trainingClassId") Long trainingClassId){
        TrainingClassResponse trainingClassResponse = trainingClassService.findById(trainingClassId);
        return new ResponseEntity<>(trainingClassResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{trainingClassId}")
    public ResponseEntity<String> deleteTrainingClass(@PathVariable("trainingClassId") Long trainingClassId){
        trainingClassService.deleteTrainingClass(trainingClassId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping
    public ResponseEntity<Void> createTrainingClass(@RequestBody TrainingClassRequest trainingClassRequest, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        trainingClassService.createTrainingClass(trainingClassRequest,authorization);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{trainingClassId}")
    public ResponseEntity<Void> updateTrainingClass(@PathVariable("trainingClassId") Long trainingClassId, @RequestBody TrainingClassRequest trainingClassRequest){
        trainingClassService.updateTrainingClass(trainingClassId,trainingClassRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
