package com.spring.project.controller;


import com.spring.project.dto.UserSubscriptionRequest;
import com.spring.project.dto.UserSubscriptionsDataResponse;
import com.spring.project.model.SubscriptionsHistory;
import com.spring.project.service.SubscriptionsHistoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(path = "project/api/v1/users")
@AllArgsConstructor
public class SubscriptionHistoryController {

    private final SubscriptionsHistoryService subscriptionsHistoryService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/{userId}/subscriptions")
    public ResponseEntity<List<UserSubscriptionsDataResponse>> getUserSubscriptions(@PathVariable("userId") Long userId){
        List<UserSubscriptionsDataResponse> userSubscriptionsDataResponses =  subscriptionsHistoryService.getUserSubscriptions(userId);
        return new ResponseEntity<>(userSubscriptionsDataResponses, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/subscriptions")
    public ResponseEntity<SubscriptionsHistory> addSubscriptionForUser(@RequestBody UserSubscriptionRequest userSubscriptionRequest){
        SubscriptionsHistory subscriptionsHistory = subscriptionsHistoryService.addSubscriptionForUser(userSubscriptionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(subscriptionsHistory);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/subscriptions/{subscriptionId}")
    public ResponseEntity<Void> addSubscriptionForUser(@PathVariable("subscriptionId") Long subscriptionId, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        subscriptionsHistoryService.addSubscriptionForUserByCard(subscriptionId, authorization);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/subscriptions")
    public ResponseEntity<List<UserSubscriptionsDataResponse>> getUserSubscriptions(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        List<UserSubscriptionsDataResponse> userSubscriptionsDataResponses = subscriptionsHistoryService.getLoggedInUserSubscriptions(authorization);
        return new ResponseEntity<>(userSubscriptionsDataResponses, HttpStatus.OK);
    }
}
