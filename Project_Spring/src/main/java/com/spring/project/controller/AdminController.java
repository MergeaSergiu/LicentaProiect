package com.spring.project.controller;

import com.spring.project.dto.*;
import com.spring.project.service.AdminService;
import com.spring.project.service.impl.ClientService;
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

    @Autowired
    private final ClientService clientService;

    @GetMapping("/secured")
    public ResponseEntity<String> getAdmin(){
        return ResponseEntity.ok("Ai accesat pagina adminului");
    }

    @GetMapping("/allReservations")
    public ResponseEntity<List<ReservationResponse>> getAllReservations(){
        return ResponseEntity.ok(adminService.getAllReservations());
    }

    @PostMapping("/createTrainer")
    public ResponseEntity<TrainerResponse> createTrainer(@RequestBody TrainerRequest trainerRequest) {
        return ResponseEntity.ok(adminService.createTrainer(trainerRequest));
    }

    @GetMapping("/users")
    public ResponseEntity<List<ClientResponse>> getAllUsers(){
        List<ClientResponse> clientResponses = adminService.getAllClients();
            return ResponseEntity.ok(clientResponses);
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
