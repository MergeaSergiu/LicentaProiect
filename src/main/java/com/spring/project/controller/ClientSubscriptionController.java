package com.spring.project.controller;


import com.spring.project.Exception.CustomExpiredJwtException;
import com.spring.project.dto.StripeChargeRequest;
import com.spring.project.dto.StripeTokenRequest;
import com.spring.project.model.Subscription;
import com.spring.project.model.TrainingClass;
import com.spring.project.service.ClientSubscriptionService;
import com.spring.project.service.StripeService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
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

    @Autowired
    private final StripeService stripeService;

    @GetMapping("/subscriptions")
    public ResponseEntity<List<Subscription>> getSubscription() {
            List<Subscription> subscriptions = clientSubscriptionService.getSubscriptions();
            return ResponseEntity.ok(subscriptions);

    }

    @PostMapping("/card/token")
    public ResponseEntity<StripeTokenRequest> createCardToken(@RequestBody StripeTokenRequest stripeTokenRequest){
        return ResponseEntity.ok(stripeService.createCardToken(stripeTokenRequest));
    }

    @PostMapping("/paySubscription")
    public ResponseEntity<StripeChargeRequest> paySubscription(@RequestParam("className") String className,
                                               @RequestBody StripeChargeRequest stripeChargeRequest){

        return ResponseEntity.ok(stripeService.charge(className, stripeChargeRequest));

    }

}
