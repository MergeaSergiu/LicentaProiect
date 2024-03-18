package com.spring.project.mapper;

import com.spring.project.dto.UserSubscriptionsDataResponse;
import com.spring.project.model.Client;
import com.spring.project.model.Subscription;
import com.spring.project.model.SubscriptionsHistory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class SubscriptionHistoryMapper {

    public SubscriptionsHistory convertFromDto(Client user, Subscription subscription){
        return SubscriptionsHistory.builder()
                .user(user)
                .subscriptionName(subscription.getSubscriptionName())
                .subscriptionPrice(subscription.getSubscriptionPrice())
                .subscriptionStartTime(LocalDate.now())
                .subscriptionEndTime(LocalDate.now().plusDays(subscription.getSubscriptionTime()))
                .build();
    }

    public UserSubscriptionsDataResponse convertToDto(SubscriptionsHistory subscriptionsHistory){
        return UserSubscriptionsDataResponse.builder()
                .subscriptionName(subscriptionsHistory.getSubscriptionName())
                .subscriptionPrice(subscriptionsHistory.getSubscriptionPrice())
                .firstName(subscriptionsHistory.getUser().getFirstName())
                .lastName(subscriptionsHistory.getUser().getLastName())
                .startDate(subscriptionsHistory.getSubscriptionStartTime())
                .endDate(subscriptionsHistory.getSubscriptionEndTime())
                .build();
    }
}
