package com.spring.project.mapper;

import com.spring.project.dto.SubscriptionResponse;
import com.spring.project.model.Subscription;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionMapper {

    public SubscriptionResponse convertToDto(Subscription subscription){
        return SubscriptionResponse.builder()
                .id(subscription.getId())
                .subscriptionName(subscription.getSubscriptionName())
                .subscriptionPrice(subscription.getSubscriptionPrice())
                .subscriptionTime(subscription.getSubscriptionTime())
                .subscriptionDescription(subscription.getSubscriptionDescription())
                .build();
    }
}
