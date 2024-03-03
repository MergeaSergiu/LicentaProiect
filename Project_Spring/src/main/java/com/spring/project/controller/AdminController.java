package com.spring.project.controller;

import com.spring.project.dto.*;
import com.spring.project.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "project/api/admin")
public class AdminController {

    @Autowired
    private final AdminService adminService;

    @GetMapping("/secured")
    public ResponseEntity<String> getAdmin(){
        return ResponseEntity.ok("Ai accesat pagina adminului");
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDataResponse>> getAllUsers(){
        List<UserDataResponse> userDataResponse = adminService.getAllClients();
        return ResponseEntity.ok(userDataResponse);
    }

    @GetMapping("/getUserData")
    public ResponseEntity<UserDataResponse> getUserData(@RequestParam("id") Integer id){
        UserDataResponse userDataResponse = adminService.getUserData(id);
        return ResponseEntity.ok(userDataResponse);
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<Void> deleteUser(@RequestParam("id") Integer id){
        adminService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/updateUserRole")
    public ResponseEntity<Void> updateUserRole(@RequestParam("id") Integer id, @RequestBody RoleRequest roleRequest){
        adminService.updateUserRole(id, roleRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/allReservations")
    public ResponseEntity<List<ReservationResponse>> getAllReservations(){
        return ResponseEntity.ok(adminService.getAllReservations());
    }

    @GetMapping("/getSubscriptions")
    public ResponseEntity<List<SubscriptionResponse>> getAllSubscriptions(){
        List<SubscriptionResponse> subscriptionResponse = adminService.getAllSubscriptions();
        return ResponseEntity.ok(subscriptionResponse);
    }

    @PostMapping("/createSubscription")
    public ResponseEntity<Void> createSubscription(@RequestBody CreateSubscriptionRequest createSubscriptionRequest){
        adminService.createSubscription(createSubscriptionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/getSubscription")
    public ResponseEntity<SubscriptionResponse> getSubscriptionById(@RequestParam("id") Integer id){
        SubscriptionResponse subscriptionResponse = adminService.getSubscriptionById(id);
        return ResponseEntity.ok(subscriptionResponse);
    }


    @PutMapping("/updateSubscription")
    public ResponseEntity<Void> updateSubscription(@RequestParam("id") Integer id, @RequestBody CreateSubscriptionRequest subscriptionRequest){
        adminService.updateSubscription(id, subscriptionRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/deleteSubscription")
    public ResponseEntity<Void> deleteSubscription(@RequestParam("id") Integer id){
        adminService.deleteSubscription(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/getTrainers")
    public ResponseEntity<List<TrainerResponse>> getAllTrainers(){
        List<TrainerResponse> trainersResponses = adminService.getAllTrainers();
        return ResponseEntity.ok(trainersResponses);
    }

    @GetMapping("/getTrainingClasses")
    public ResponseEntity<List<TrainingClassResponse>> getTrainingClasses(){
        List<TrainingClassResponse> trainingClassResponse = adminService.getAllTrainingClasses();
        return ResponseEntity.ok(trainingClassResponse);
    }

    @GetMapping("/getTrainingClassById")
    public ResponseEntity<TrainingClassResponse> getTrainingClass(@RequestParam("id") Integer id){
        TrainingClassResponse trainingClassResponse = adminService.getTrainingClass(id);
        return ResponseEntity.ok(trainingClassResponse);
    }

    @DeleteMapping("/deleteClass")
    public ResponseEntity<String> deleteTrainingClass(@RequestParam("id") Integer id){
        adminService.deleteTrainingClass(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/createTrainingClass")
    public ResponseEntity<Void> createTrainingClass(@RequestBody TrainingClassRequest classRequest) {
        adminService.createTrainingClass(classRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/updateClass")
    public ResponseEntity<Void> updateTrainingClass(@RequestParam("id") Integer id, @RequestBody TrainingClassRequest trainingClassRequest){
        adminService.updateTrainingClass(id,trainingClassRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }



}
