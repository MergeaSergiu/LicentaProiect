package com.spring.project.service.impl;

import com.spring.project.Exception.CustomExpiredJwtException;
import com.spring.project.dto.SubscriptionResponse;
import com.spring.project.model.Subscription;
import com.spring.project.service.ClientSubscriptionService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ClientSubscriptionServiceImpl implements ClientSubscriptionService {

    private final SubscriptionServiceImpl subscriptionServiceImpl;

    public List<SubscriptionResponse> getSubscriptions() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication.isAuthenticated()) {
                List<Subscription> subscriptions = subscriptionServiceImpl.getAllSubscriptionPlans();
                return subscriptions.stream()
                        .map(subscription -> SubscriptionResponse.builder()
                                .subscriptionName(subscription.getSubscriptionName())
                                .subscriptionPrice(subscription.getSubscriptionPrice())
                                .subscriptionTime(subscription.getSubscriptionTime())
                                .subscriptionDescription(subscription.getSubscriptionDescription())
                                .build())
                        .collect(Collectors.toList());
            }else {
                throw new CustomExpiredJwtException("Session expired");
            }
    }
}
