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
@RequestMapping(path = "project/api/v1/users")
public class UserController {

    @Autowired
    private final AdminService adminService;

    @GetMapping
    public ResponseEntity<List<UserDataResponse>> getAllUsers(){
        List<UserDataResponse> userDataResponse = adminService.getAllClients();
        return ResponseEntity.ok(userDataResponse);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDataResponse> getUserData(@PathVariable("userId") Integer userId){
        UserDataResponse userDataResponse = adminService.getUserData(userId);
        return ResponseEntity.ok(userDataResponse);
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") Integer userId){
        adminService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{userId}/role")
    public ResponseEntity<Void> updateUserRole(@PathVariable("userId") Integer userId, @RequestBody RoleRequest roleRequest){
        adminService.updateUserRole(userId, roleRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/trainers")
    public ResponseEntity<List<TrainerResponse>> getAllTrainers(){
        List<TrainerResponse> trainersResponses = adminService.getAllTrainers();
        return ResponseEntity.ok(trainersResponses);
    }

    @GetMapping("/{userId}/subscriptions")
    public ResponseEntity<List<UserSubscriptionsDataResponse>> getUserSubscriptions(@PathVariable("userId") Integer userId){
        List<UserSubscriptionsDataResponse> userSubscriptionsDataResponses =  adminService.getUserSubscriptionsData(userId);
        return ResponseEntity.ok(userSubscriptionsDataResponses);
    }

    @PostMapping("/subscriptions")
    public ResponseEntity<Void> addSubscriptionForUser(@RequestBody UserSubscriptionRequest userSubscriptionRequest){
        adminService.addSubscriptionForUser(userSubscriptionRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/subscriptions/{subscriptionId}")
    public ResponseEntity<Void> addSubscriptionForUser(@PathVariable("subscriptionId") Integer subscriptionId){
        adminService.addSubscriptionForUserByCard(subscriptionId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/subscriptions")
    public ResponseEntity<List<UserSubscriptionsDataResponse>> getUserSubscriptions(){
        List<UserSubscriptionsDataResponse> userSubscriptionsDataResponses = adminService.getUserSubscriptionsData();
        return ResponseEntity.ok(userSubscriptionsDataResponses);
    }

}
