package com.spring.project.controller;

import com.spring.project.dto.*;
import com.spring.project.service.TrainerService;
import com.spring.project.service.UserAccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping(path = "/project/api/v1/users")
public class UserAccountController {

    private final UserAccountService userAccountService;

    private final TrainerService trainerService;

    @GetMapping("/profile")
    public ResponseEntity<UserDataResponse> getUserProfileData(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        UserDataResponse userDataResponse = userAccountService.getUserProfileData(authorization);
        return new ResponseEntity<>(userDataResponse,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('TRAINER')")
    @GetMapping("/trainer/classes")
    public ResponseEntity<List<TrainingClassResponse>> getTrainingClasses(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        List<TrainingClassResponse> trainingClassesForTrainer = trainerService.getTrainingClassesForTrainer(authorization);
        return new ResponseEntity<>(trainingClassesForTrainer, HttpStatus.OK);
    }


    @PutMapping
    public ResponseEntity<Void> updateUserProfile(@RequestBody UpdateUserRequest updateUserRequest, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        userAccountService.updateUserProfile(updateUserRequest, authorization);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/activeSubscriptions")
    public ResponseEntity<?> getUserActiveSubscriptions(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        boolean hasActiveSubscription = userAccountService.getUserActiveSubscriptions(authorization);
        return ResponseEntity.ok().body("{\"hasActiveSubscription\": " + hasActiveSubscription + "}");
    }


}
