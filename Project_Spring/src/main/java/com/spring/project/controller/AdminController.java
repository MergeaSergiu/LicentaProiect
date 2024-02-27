package com.spring.project.controller;

import com.spring.project.dto.*;
import com.spring.project.service.AdminService;
import com.spring.project.service.RegistrationService;
import com.spring.project.service.impl.ClientService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @PostMapping("/createClass")
    public ResponseEntity<String> createTrainingClass(@RequestBody @Valid CreateClassRequest classRequest) {
            if (clientService.findClientByEmail(classRequest.getTrainerEmail()) != null) {
                if (clientService.findClientByEmail(classRequest.getTrainerEmail()).getClientRole().toString().equals("TRAINER")) {
                    adminService.createTrainingClass(classRequest);
                    return ResponseEntity.status(HttpStatus.CREATED).build();
                }else{
                    throw new EntityNotFoundException("There is no account with this email");
                }
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PatchMapping("/updateClass/{id}")
    public ResponseEntity<String> updateTrainingClass(@PathVariable("id") Integer id, @RequestBody Map<String, Object> fields){
        adminService.updateTrainingClass(id,fields);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/deleteClass")
    public ResponseEntity<String> deleteTrainingClass(@RequestParam("className") String className){
        adminService.deleteTrainingClass(className);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
