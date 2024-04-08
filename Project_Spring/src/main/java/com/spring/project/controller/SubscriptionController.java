package com.spring.project.controller;

import com.spring.project.Exception.CustomExpiredJwtException;
import com.spring.project.dto.CreateSubscriptionRequest;
import com.spring.project.dto.SubscriptionResponse;
import com.spring.project.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(path = "project/api/v1/subscriptions")
@AllArgsConstructor
public class SubscriptionController {

    @Autowired
    private final AdminService adminService;

    @GetMapping
    public ResponseEntity<List<SubscriptionResponse>> getAllSubscriptions(){
        List<SubscriptionResponse> subscriptionResponse = adminService.getAllSubscriptions();
        return new ResponseEntity<>(subscriptionResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> createSubscription(@RequestBody CreateSubscriptionRequest createSubscriptionRequest){
        adminService.createSubscription(createSubscriptionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{subscriptionId}")
    public ResponseEntity<SubscriptionResponse> getSubscriptionById(@PathVariable("subscriptionId") Long subscriptionId){
        SubscriptionResponse subscriptionResponse = adminService.getSubscriptionById(subscriptionId);
        return new ResponseEntity<>(subscriptionResponse, HttpStatus.OK);
    }


    @PutMapping("/{subscriptionId}")
    public ResponseEntity<Void> updateSubscription(@PathVariable("subscriptionId") Long subscriptionId, @RequestBody CreateSubscriptionRequest subscriptionRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.isAuthenticated()){
            throw new CustomExpiredJwtException("Session has expred");
        }
        adminService.updateSubscription(subscriptionId, subscriptionRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{subscriptionId}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable("subscriptionId") Long subscriptionId){
        adminService.deleteSubscription(subscriptionId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
