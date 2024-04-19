package com.spring.project.controller;

import com.spring.project.dto.*;
import com.spring.project.service.TrainerService;
import com.spring.project.service.UserAccountService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping(path = "/project/api/v1/users")
public class UserAccountController {

    @Autowired
    private final UserAccountService userAccountService;

    @Autowired
    private final TrainerService trainerService;


    @GetMapping("/profile")
    public ResponseEntity<UserDataResponse> getUserProfileData(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        UserDataResponse userDataResponse = userAccountService.getUserProfileData(authorization);
        return new ResponseEntity<>(userDataResponse,HttpStatus.OK);
    }
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

    @GetMapping("/activeSubscriptions")
    public ResponseEntity<?> getUserActiveSubscriptions(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        boolean hasActiveSubscription = userAccountService.getUserActiveSubscriptions(authorization);
        return ResponseEntity.ok().body("{\"hasActiveSubscription\": " + hasActiveSubscription + "}");
    }


}
