package com.spring.project.service.impl;

import com.spring.project.model.Subscription;
import com.spring.project.repository.SubscriptionRepository;
import com.spring.project.service.SubscriptionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private final SubscriptionRepository subscriptionRepository;
    public void saveSubscription(Subscription createSubscriptionRequest){
        subscriptionRepository.save(createSubscriptionRequest);
    }

    public List<Subscription> getAllSubscriptionPlans(){
        return subscriptionRepository.findAll();
    }

    public Optional<Subscription> findById(Long id){
        return subscriptionRepository.findById(id);
    }

    public Subscription findBySubscriptionName(String subscriptionName){
        return subscriptionRepository.findBySubscriptionName(subscriptionName);
    }

    public void deleteSubscription(Long id) {
        subscriptionRepository.deleteById(id);
    }
}
