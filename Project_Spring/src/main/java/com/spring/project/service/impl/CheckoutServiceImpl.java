package com.spring.project.service.impl;

import com.spring.project.dto.PaymentRequest;
import com.spring.project.mapper.CardDataValidation;
import com.spring.project.model.User;
import com.spring.project.service.CheckoutService;
import com.spring.project.util.UtilMethods;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;

import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class CheckoutServiceImpl implements CheckoutService {

    private final UtilMethods utilMethods;
    private final CardDataValidation cardDataValidation;

    public CheckoutServiceImpl(UtilMethods utilMethods, CardDataValidation cardDataValidation, @Value("${stripe.key.secret}") String secretKey) {
        this.utilMethods = utilMethods;
        Stripe.apiKey = secretKey;
        this.cardDataValidation = cardDataValidation;
    }

    @Override
    public PaymentIntent createPaymentIntent(PaymentRequest paymentRequest, String authorization) throws StripeException {
        User user = utilMethods.extractUsernameFromAuthorizationHeader(authorization);
        cardDataValidation.cardValidation(authorization,paymentRequest);
        CustomerCreateParams CustomerParams = CustomerCreateParams.builder()
                .setName(paymentRequest.getCardHolderName())
                .setEmail(user.getEmail())
                .build();
        String customerId = Customer.create(CustomerParams).getId();
        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount((long) paymentRequest.getAmount())
                        .setCurrency(paymentRequest.getCurrency())
                        .setPaymentMethod("pm_card_visa")
                        .setAutomaticPaymentMethods(
                                PaymentIntentCreateParams.AutomaticPaymentMethods
                                        .builder()
                                        .setEnabled(true)
                                        .build()
                        )
                        .setCustomer(customerId)
                        .setDescription("Subscription Purchase")
                        .build();
        return PaymentIntent.create(params);
    }

}
