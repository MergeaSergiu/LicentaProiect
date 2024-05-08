package com.spring.project.controller;

import com.spring.project.dto.CreateSubscriptionRequest;
import com.spring.project.dto.SubscriptionResponse;
import com.spring.project.service.SubscriptionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(path = "project/api/v1/subscriptions")
@AllArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<SubscriptionResponse>> getAllSubscriptions(){
        List<SubscriptionResponse> subscriptionResponse = subscriptionService.getAllSubscriptionPlans();
        return new ResponseEntity<>(subscriptionResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> createSubscription(@RequestBody CreateSubscriptionRequest createSubscriptionRequest){
        subscriptionService.saveSubscription(createSubscriptionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{subscriptionId}")
    public ResponseEntity<SubscriptionResponse> getSubscriptionById(@PathVariable("subscriptionId") Long subscriptionId){
        SubscriptionResponse subscriptionResponse = subscriptionService.getSubscriptionById(subscriptionId);
        Double a = subscriptionResponse.getSubscriptionPrice();
        return new ResponseEntity<>(subscriptionResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{subscriptionId}")
    public ResponseEntity<Void> updateSubscription(@PathVariable("subscriptionId") Long subscriptionId, @RequestBody CreateSubscriptionRequest subscriptionRequest){
        subscriptionService.updateSubscription(subscriptionId, subscriptionRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{subscriptionId}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable("subscriptionId") Long subscriptionId){
        subscriptionService.deleteSubscription(subscriptionId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
