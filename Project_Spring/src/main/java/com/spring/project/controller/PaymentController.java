package com.spring.project.controller;

import com.spring.project.dto.PaymentRequest;
import com.spring.project.service.CheckoutService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/project/api/v1/payment")
public class PaymentController {

    public PaymentController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    private final CheckoutService checkoutService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/payment-intent")
    public ResponseEntity<String> createPaymentIntent(@RequestBody PaymentRequest paymentRequest, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) throws StripeException {

        PaymentIntent paymentIntent = checkoutService.createPaymentIntent(paymentRequest, authorization);
        return new ResponseEntity<>(paymentIntent.toJson(), HttpStatus.CREATED);
    }
}
