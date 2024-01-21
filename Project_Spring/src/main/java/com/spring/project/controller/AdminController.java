package com.spring.project.controller;

import com.spring.project.dto.*;
import com.spring.project.service.AdminService;
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

    @PostMapping("/createTrainer")
    public ResponseEntity<TrainerResponse> createTrainer(@Valid @RequestBody TrainerRequest trainerRequest) {
        return ResponseEntity.ok(adminService.createTrainer(trainerRequest));
    }

    @GetMapping("/users")
    public ResponseEntity<List<ClientResponse>> getAllUsers(){
        List<ClientResponse> clientResponses = adminService.getAllClients();
            return ResponseEntity.ok(clientResponses);
    }

    @PostMapping("/createSubscription")
    public ResponseEntity<String> createSubscription(@RequestBody @Valid CreateSubscriptionRequest createSubscriptionRequest){
        adminService.createSubscription(createSubscriptionRequest);
        return new ResponseEntity<>("Subscription created successfully", HttpStatus.CREATED);
    }

    @PatchMapping("/updateSubscription/{id}")
    public ResponseEntity<String> updateSubscription(@PathVariable("id") Integer id, @RequestBody Map<String, Object> fields){
        adminService.updateSubscription(id, fields);
        return ResponseEntity.ok("Subscription data was update");
    }

    @DeleteMapping("/deleteSubscription/{id}")
    public ResponseEntity<String> deleteSubscription(@PathVariable("id") Integer id){
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
