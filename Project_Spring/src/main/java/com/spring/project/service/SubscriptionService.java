package com.spring.project.service;

import com.spring.project.model.Subscription;

import java.util.List;
import java.util.Optional;

public interface SubscriptionService {

    void saveSubscription(Subscription createSubscriptionRequest);

    List<Subscription> getAllSubscriptionPlans();

    Optional<Subscription> findById(Long id);

    Subscription findBySubscriptionName(String subscriptionName);

    void deleteSubscription(Long id);
}
