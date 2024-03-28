package com.spring.project.controller;

import com.spring.project.dto.*;
import com.spring.project.service.TrainerService;
import com.spring.project.service.UserAccountService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping(path = "/project/api/v1/users")
public class UserAccountController {

    @Autowired
    private final UserAccountService userAccountService;

    @Autowired
    private final TrainerService trainerService;
    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponse>> getReservationHistory(){
        List<ReservationResponse> reservations = userAccountService.getAllClientReservations();
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDataResponse> getUserProfileData(){
        UserDataResponse userDataResponse = userAccountService.getUserProfileData();
        return new ResponseEntity<>(userDataResponse,HttpStatus.OK);
    }
    @GetMapping("/trainer/classes")
    public ResponseEntity<List<TrainingClassResponse>> getTrainingClasses(){
        List<TrainingClassResponse> trainingClassesForTrainer = trainerService.getTrainingClassesForTrainer();
        return new ResponseEntity<>(trainingClassesForTrainer, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Void> updateUserProfile(@RequestBody UpdateUserRequest updateUserRequest){
        userAccountService.updateUserProfile(updateUserRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/activeSubscriptions")
    public ResponseEntity<?> getUserActiveSubscriptions(){
        boolean hasActiveSubscription = userAccountService.getUserActiveSubscriptions();
        return ResponseEntity.ok().body("{\"hasActiveSubscription\": " + hasActiveSubscription + "}");
    }


}
