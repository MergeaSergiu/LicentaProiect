package com.spring.project.mapper;

import com.spring.project.dto.CreateSubscriptionRequest;
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

    public Subscription convertFromDto(CreateSubscriptionRequest subscriptionRequest){
        return Subscription.builder()
                .subscriptionName(subscriptionRequest.getSubscriptionName())
                .subscriptionPrice(subscriptionRequest.getSubscriptionPrice())
                .subscriptionName(subscriptionRequest.getSubscriptionName())
                .subscriptionTime(subscriptionRequest.getSubscriptionTime())
                .subscriptionDescription(subscriptionRequest.getSubscriptionDescription())
                .build();
    }
}
