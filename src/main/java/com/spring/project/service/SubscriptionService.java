package com.spring.project.service;

import com.spring.project.model.Subscription;
import com.spring.project.repository.SubscriptionRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SubscriptionService {

    @Autowired
    private final SubscriptionRepository subscriptionRepository;
    public void saveSubscription(Subscription createSubscriptionRequest){
        subscriptionRepository.save(createSubscriptionRequest);
    }

    public List<Subscription> getAllSubscriptionPlans(){
        return subscriptionRepository.findAllSubscriptions();
    }

    public Optional<Subscription> findById(Integer id){
        return subscriptionRepository.findById(id);

    }

    public void deleteSubscription(Integer id) {
        subscriptionRepository.deleteById(id);
    }
}
