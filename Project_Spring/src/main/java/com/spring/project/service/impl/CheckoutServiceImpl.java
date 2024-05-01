package com.spring.project.service.impl;

import com.spring.project.dto.PaymentRequest;
import com.spring.project.model.User;
import com.spring.project.repository.UserRepository;
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
    private final UserRepository userRepository;

 public CheckoutServiceImpl(UtilMethods utilMethods, UserAccountService userAccountService, @Value("${stripe.key.secret}") String secretKey, UserRepository userRepository){
     this.utilMethods = utilMethods;
     this.userAccountService = userAccountService;
     this.userRepository = userRepository;
     Stripe.apiKey = secretKey;
 }

    @Override
    public PaymentIntent createPaymentIntent(PaymentRequest paymentRequest, String authorization) throws StripeException {

            String username = utilMethods.extractUsernameFromAuthorizationHeader(authorization);
            User user = userRepository.findByEmail(username).orElse(null);
            if(user == null){
                throw new EntityNotFoundException("User does not exist");
            }
            boolean hasActiveSubscription = userAccountService.getUserActiveSubscriptions(authorization);
            if(hasActiveSubscription){
                throw new EntityExistsException("User has an active subscription. We can not proceed the payment");
            }
            CustomerCreateParams CustomerParams = CustomerCreateParams.builder()
                    .setName(paymentRequest.getCardHolderName())
                    .setEmail(username)
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
