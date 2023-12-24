package com.spring.project.controller;


import com.spring.project.Exception.CustomExpiredJwtException;
import com.spring.project.model.Subscription;
import com.spring.project.service.ClientSubscriptionService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping(path = "/project/api/user")
public class ClientSubscriptionController {

    @Autowired
    private final ClientSubscriptionService clientSubscriptionService;

    @GetMapping("/subscriptions")
    public ResponseEntity<List<Subscription>> getSubscription() {
            List<Subscription> subscriptions = clientSubscriptionService.getSubscriptions();
            return ResponseEntity.ok(subscriptions);

    }
}
