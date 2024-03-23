package com.spring.project.service.impl;

import com.spring.project.Exception.ClientNotFoundException;
import com.spring.project.dto.PaymentRequest;
import com.spring.project.model.Client;
import com.spring.project.repository.ClientRepository;
import com.spring.project.repository.SubscriptionHistoryRepository;
import com.spring.project.service.CheckoutService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;

import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class CheckoutServiceImpl implements CheckoutService {


    @Autowired
    private ClientRepository clientRepository;
 public CheckoutServiceImpl(ClientRepository clientRepository,
                            @Value("${stripe.key.secret}") String secretKey){
     this.clientRepository = clientRepository;
     Stripe.apiKey = secretKey;
 }

    @Override
    public PaymentIntent createPaymentIntent(PaymentRequest paymentRequest) throws StripeException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Client user = clientRepository.findByEmail(authentication.getName()).orElse(null);
        if (user != null) {
            CustomerCreateParams Customerparams = CustomerCreateParams.builder()
                    .setName(user.getLastName() + " " + user.getFirstName())
                    .setEmail(authentication.getName())
                    .build();

            String customerId = Customer.create(Customerparams).getId();

            PaymentIntentCreateParams params =
                    PaymentIntentCreateParams.builder()
                            .setAmount(Long.valueOf(paymentRequest.getAmount()))
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
        }else{
            throw new ClientNotFoundException("There is no user");
        }
    }

}
