package com.spring.project.controller;

import com.spring.project.dto.CreateSubscriptionRequest;
import com.spring.project.dto.SubscriptionResponse;
import com.spring.project.service.SubscriptionService;
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
    private final SubscriptionService subscriptionService;

    @GetMapping
    public ResponseEntity<List<SubscriptionResponse>> getAllSubscriptions(){
        List<SubscriptionResponse> subscriptionResponse = subscriptionService.getAllSubscriptionPlans();
        return new ResponseEntity<>(subscriptionResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> createSubscription(@RequestBody CreateSubscriptionRequest createSubscriptionRequest){
        subscriptionService.saveSubscription(createSubscriptionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{subscriptionId}")
    public ResponseEntity<SubscriptionResponse> getSubscriptionById(@PathVariable("subscriptionId") Long subscriptionId){
        SubscriptionResponse subscriptionResponse = subscriptionService.getSubscriptionById(subscriptionId);
        Double a = subscriptionResponse.getSubscriptionPrice();
        return new ResponseEntity<>(subscriptionResponse, HttpStatus.OK);
    }

    @PutMapping("/{subscriptionId}")
    public ResponseEntity<Void> updateSubscription(@PathVariable("subscriptionId") Long subscriptionId, @RequestBody CreateSubscriptionRequest subscriptionRequest){
        subscriptionService.updateSubscription(subscriptionId, subscriptionRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{subscriptionId}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable("subscriptionId") Long subscriptionId){
        subscriptionService.deleteSubscription(subscriptionId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
