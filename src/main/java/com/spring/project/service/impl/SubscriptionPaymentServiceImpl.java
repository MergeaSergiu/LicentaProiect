package com.spring.project.service.impl;

import com.spring.project.model.SubscriptionPayment;
import com.spring.project.repository.SubscriptionPaymentRespository;
import com.spring.project.service.SubscriptionPaymentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SubscriptionPaymentServiceImpl implements SubscriptionPaymentService {

    @Autowired
    private final SubscriptionPaymentRespository subscriptionPaymentRespository;

    public void createSubscriptionPayment(SubscriptionPayment subscriptionPayment){
        subscriptionPaymentRespository.save(subscriptionPayment);
    }
}
