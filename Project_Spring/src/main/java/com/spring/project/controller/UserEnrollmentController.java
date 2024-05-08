package com.spring.project.controller;

import com.spring.project.dto.TrainingClassResponse;
import com.spring.project.service.EnrollmentTrainingClassService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping(path = "project/api/v1/users")
public class UserEnrollmentController {

    private final EnrollmentTrainingClassService enrollmentTrainingClassService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/classes/{trainingClassId}")
    public ResponseEntity<Void> enrollUserToTrainingClass(@PathVariable("trainingClassId") Long trainingClassId, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        enrollmentTrainingClassService.saveEnrollmentAction(trainingClassId, authorization);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/classes")
    public ResponseEntity<List<TrainingClassResponse>> getEnrollClassesForUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        List<TrainingClassResponse> trainingClassResponseList = enrollmentTrainingClassService.getClassesForUser(authorization);
        return new ResponseEntity<>(trainingClassResponseList, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/classes/{trainingClassId}")
    public ResponseEntity<Void> UnEnrollUserFromTrainingClass(@PathVariable("trainingClassId") Long trainingClassId, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        enrollmentTrainingClassService.deleteEnrollmentForUser(trainingClassId,authorization);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
