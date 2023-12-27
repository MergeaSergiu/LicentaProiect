package com.spring.project.controller;


import com.spring.project.dto.SubscriptionResponse;
import com.spring.project.service.ClientSubscriptionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping(path = "/project/api/user")
public class ClientSubscriptionController {

    @Autowired
    private final ClientSubscriptionService clientSubscriptionService;

    @GetMapping("/subscriptions")
    public ResponseEntity<List<SubscriptionResponse>> getSubscription() {
            List<SubscriptionResponse> subscriptions = clientSubscriptionService.getSubscriptions();
            return ResponseEntity.ok(subscriptions);
    }



}
