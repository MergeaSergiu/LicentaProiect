package com.spring.project.controller;

import com.spring.project.dto.PaymentRequest;
import com.spring.project.service.CheckoutService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/project/api/user")
public class PaymentController {

    public PaymentController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    private final CheckoutService checkoutService;

    @PostMapping("/payment-intent")
    public ResponseEntity<String> createPaymentIntent(@RequestBody PaymentRequest paymentRequest) throws StripeException {

        PaymentIntent paymentIntent = checkoutService.createPaymentIntent(paymentRequest);
        return new ResponseEntity<>(paymentIntent.toJson(), HttpStatus.OK);
    }
}
