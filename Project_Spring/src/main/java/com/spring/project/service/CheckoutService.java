package com.spring.project.service;


import com.spring.project.dto.PaymentRequest;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

public interface CheckoutService {

    PaymentIntent createPaymentIntent(PaymentRequest paymentRequest) throws StripeException;


}
