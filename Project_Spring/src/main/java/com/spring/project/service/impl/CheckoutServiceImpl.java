package com.spring.project.service.impl;

import com.spring.project.dto.PaymentRequest;
import com.spring.project.model.Subscription;
import com.spring.project.model.User;
import com.spring.project.repository.SubscriptionRepository;
import com.spring.project.service.CheckoutService;
import com.spring.project.service.UserAccountService;
import com.spring.project.util.UtilMethods;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;

import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class CheckoutServiceImpl implements CheckoutService {

    private final UtilMethods utilMethods;
    private final UserAccountService userAccountService;
    private final SubscriptionRepository subscriptionRepository;

    public CheckoutServiceImpl(UtilMethods utilMethods, UserAccountService userAccountService,SubscriptionRepository subscriptionRepository, @Value("${stripe.key.secret}") String secretKey) {
        this.utilMethods = utilMethods;
        this.userAccountService = userAccountService;
        this.subscriptionRepository = subscriptionRepository;
        Stripe.apiKey = secretKey;
    }

    @Override
    public PaymentIntent createPaymentIntent(PaymentRequest paymentRequest, String authorization) throws StripeException {
        User user = utilMethods.extractUsernameFromAuthorizationHeader(authorization);
        boolean hasActiveSubscription = userAccountService.getUserActiveSubscriptions(authorization);
        if (paymentRequest.getCardHolderName() == null || paymentRequest.getCardHolderName().trim().isEmpty()) {
            throw new EntityNotFoundException("No cardHolderName is used");
        }
        Subscription subscription = subscriptionRepository.findById(paymentRequest.getSubscriptionId()).orElseThrow(() -> new EntityNotFoundException("There is no subscription associated with this payment"));
        double subscriptionPrice = (double) paymentRequest.getAmount() /100;
        if(subscriptionPrice != subscription.getSubscriptionPrice()){
            throw new EntityNotFoundException("The price is not the same with the subscription price");
        }

        if(!paymentRequest.getCurrency().equals("ron")){
            throw new EntityNotFoundException("Price must be set for 'RON' currency");
        }
        if (hasActiveSubscription) {
            throw new EntityExistsException("User has an active subscription. We can not proceed the payment");
        }
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
