package com.spring.project.service;

import com.spring.project.dto.UserSubscriptionRequest;
import com.spring.project.dto.UserSubscriptionsDataResponse;
import com.spring.project.model.SubscriptionsHistory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SubscriptionsHistoryService {
    SubscriptionsHistory addSubscriptionForUser(UserSubscriptionRequest userSubscriptionRequest);

    List<UserSubscriptionsDataResponse> getLoggedInUserSubscriptions(String authorization);

    List<UserSubscriptionsDataResponse> getUserSubscriptions(Long id);

    void addSubscriptionForUserByCard(Long subscriptionId, String authorization);

}
