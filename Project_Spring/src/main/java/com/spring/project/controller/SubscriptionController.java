package com.spring.project.controller;

import com.spring.project.dto.CreateSubscriptionRequest;
import com.spring.project.dto.SubscriptionResponse;
import com.spring.project.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        return ResponseEntity.ok(subscriptionResponse);
    }

    @PostMapping
    public ResponseEntity<Void> createSubscription(@RequestBody CreateSubscriptionRequest createSubscriptionRequest){
        adminService.createSubscription(createSubscriptionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{subscriptionId}")
    public ResponseEntity<SubscriptionResponse> getSubscriptionById(@PathVariable("subscriptionId") Long subscriptionId){
        SubscriptionResponse subscriptionResponse = adminService.getSubscriptionById(subscriptionId);
        return ResponseEntity.ok(subscriptionResponse);
    }


    @PutMapping("/{subscriptionId}")
    public ResponseEntity<Void> updateSubscription(@RequestParam("subscriptionId") Long subscriptionId, @RequestBody CreateSubscriptionRequest subscriptionRequest){
        adminService.updateSubscription(subscriptionId, subscriptionRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{subscriptionId}")
    public ResponseEntity<Void> deleteSubscription(@RequestParam("subscriptionId") Long subscriptionId){
        adminService.deleteSubscription(subscriptionId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
