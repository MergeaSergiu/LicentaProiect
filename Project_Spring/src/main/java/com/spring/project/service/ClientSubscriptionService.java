package com.spring.project.service;

import com.spring.project.dto.SubscriptionResponse;

import java.util.List;

public interface ClientSubscriptionService {

    List<SubscriptionResponse> getSubscriptions();
}
