package com.spring.project.service;

import com.spring.project.dto.CreateSubscriptionRequest;
import com.spring.project.dto.SubscriptionResponse;
import java.util.List;

public interface SubscriptionService {

    void saveSubscription(CreateSubscriptionRequest createSubscriptionRequest);

    List<SubscriptionResponse> getAllSubscriptionPlans();

    SubscriptionResponse getSubscriptionById(Long id);

    void updateSubscription(Long id, CreateSubscriptionRequest subscriptionRequest);
    void deleteSubscription(Long id);
}
