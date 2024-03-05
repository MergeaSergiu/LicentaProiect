package com.spring.project.controller;

import com.spring.project.dto.StatusEnrollResponse;
import com.spring.project.dto.TrainingClassResponse;
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
    public ResponseEntity<Void> enrollUserToTrainingClass(@RequestParam("id") Integer id){
        clientService.enrollUserToTrainingClass(id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/checkEnrollmentStatus")
    public ResponseEntity<StatusEnrollResponse> checkEnrollmentStatus(@RequestParam ("trainingClassId") Integer trainingClassId){
        StatusEnrollResponse isEnrolled = clientService.checkEnrollmentStatus(trainingClassId);
        return ResponseEntity.ok(isEnrolled);
    }

    @DeleteMapping("/unEnroll")
    public ResponseEntity<Void> UnenrollUserFromTrainingClass(@RequestParam("classId") Integer classId){
        clientService.unEnrollUserFromTrainingClass(classId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
