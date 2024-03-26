package com.spring.project.controller;

import com.spring.project.dto.StatusEnrollResponse;
import com.spring.project.dto.TrainingClassResponse;
import com.spring.project.service.UserAccountService;
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
@RequestMapping(path = "project/api/v1/users")
public class UserEnrollmentController {

    @Autowired
    private final ClientService clientService;

    @Autowired
    private final UserAccountService userAccountService;

    @PostMapping("/classes/{trainingClassId}")
    public ResponseEntity<Void> enrollUserToTrainingClass(@PathVariable("trainingClassId") Long trainingClassId){
        clientService.enrollUserToTrainingClass(trainingClassId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/classes/{trainingClassId}")
    public ResponseEntity<StatusEnrollResponse> checkEnrollmentStatus(@PathVariable ("trainingClassId") Long trainingClassId){
        StatusEnrollResponse isEnrolled = clientService.checkEnrollmentStatus(trainingClassId);
        return new ResponseEntity<>(isEnrolled, HttpStatus.OK);
    }

    @GetMapping("/classes")
    public ResponseEntity<List<TrainingClassResponse>> getEnrollClassesForUser(){
        List<TrainingClassResponse> trainingClassResponseList = userAccountService.getEnrollClasses();
        return new ResponseEntity<>(trainingClassResponseList, HttpStatus.OK);
    }

    @DeleteMapping("/classes/{trainingClassId}")
    public ResponseEntity<Void> UnenrollUserFromTrainingClass(@PathVariable("trainingClassId") Long trainingClassId){
        clientService.unEnrollUserFromTrainingClass(trainingClassId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
