package com.spring.project.service;

import com.spring.project.model.SubscriptionPayment;
import com.spring.project.repository.SubscriptionPaymentRespository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SubscriptionPaymentService {

    @Autowired
    private final SubscriptionPaymentRespository subscriptionPaymentRespository;

    public void createSubscriptionPayment(SubscriptionPayment subscriptionPayment){
        subscriptionPaymentRespository.save(subscriptionPayment);
    }
}
