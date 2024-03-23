package com.spring.project.controller;

import com.spring.project.dto.ReservationResponse;
import com.spring.project.dto.TrainingClassResponse;
import com.spring.project.dto.UpdateUserRequest;
import com.spring.project.dto.UserDataResponse;
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
@RequestMapping(path = "/project/api/user")
public class ClientAccountController {

    @Autowired
    private final UserAccountService userAccountService;

    @Autowired
    private final TrainerService trainerService;
    @GetMapping("/clientReservations")
    public ResponseEntity<List<ReservationResponse>> getReservationHistory(){
        List<ReservationResponse> reservations = userAccountService.getAllClientReservations();
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/getUserProfileData")
    public ResponseEntity<UserDataResponse> getUserProfileData(){
        UserDataResponse userDataResponse = userAccountService.getUserProfileData();
        return ResponseEntity.ok(userDataResponse);
    }
    @GetMapping("/getTrainerClasses")
    public ResponseEntity<List<TrainingClassResponse>> getTrainingClasses(){
        List<TrainingClassResponse> trainingClassesForTrainer = trainerService.getTrainingClassesForTrainer();
        return ResponseEntity.ok(trainingClassesForTrainer);
    }

    @PutMapping("/updateUserProfile")
    public ResponseEntity<Void> updateUserProfile(@RequestBody UpdateUserRequest updateUserRequest){
        userAccountService.updateUserProfile(updateUserRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @GetMapping("/getEnrollClasses")
    public ResponseEntity<List<TrainingClassResponse>> getEnrollClassesForUser(){
        return ResponseEntity.ok(userAccountService.getEnrollClasses());
    }


}
