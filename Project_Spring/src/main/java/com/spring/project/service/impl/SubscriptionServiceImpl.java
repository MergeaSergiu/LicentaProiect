package com.spring.project.service.impl;

import com.spring.project.dto.CreateSubscriptionRequest;
import com.spring.project.dto.SubscriptionResponse;
import com.spring.project.mapper.SubscriptionMapper;
import com.spring.project.model.Subscription;
import com.spring.project.repository.SubscriptionRepository;
import com.spring.project.service.SubscriptionService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;

    public void saveSubscription(CreateSubscriptionRequest createSubscriptionRequest) {
        Subscription foundsubscription = subscriptionRepository.findBySubscriptionName(createSubscriptionRequest.getSubscriptionName());
        if (foundsubscription != null) {
            throw new EntityExistsException("There is already a subscription with this name");
        }
        Subscription subscription = subscriptionMapper.convertFromDto(createSubscriptionRequest);
        subscriptionRepository.save(subscription);
    }

    public List<SubscriptionResponse> getAllSubscriptionPlans() {
        return subscriptionRepository.findAll().stream()
                .map(subscriptionMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public void updateSubscription(Long id, CreateSubscriptionRequest subscriptionRequest) {
        Subscription currentSubscription = subscriptionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Subscription does not exist"));
        Subscription foundSubscriptionByName = subscriptionRepository.findBySubscriptionName(subscriptionRequest.getSubscriptionName());
        if (foundSubscriptionByName != null && !subscriptionRequest.getSubscriptionName().equals(currentSubscription.getSubscriptionName())) {
            throw new EntityExistsException("A subscription with the same name exists already");
        }
        currentSubscription.setSubscriptionName(subscriptionRequest.getSubscriptionName());
        currentSubscription.setSubscriptionPrice(subscriptionRequest.getSubscriptionPrice());
        currentSubscription.setSubscriptionDescription(subscriptionRequest.getSubscriptionDescription());
        subscriptionRepository.save(currentSubscription);
    }

    public SubscriptionResponse getSubscriptionById(Long id) {
        Subscription subscription = subscriptionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Subscription does not exist"));
        return subscriptionMapper.convertToDto(subscription);
    }

    public void deleteSubscription(Long id) {
        Subscription subscription = subscriptionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Subscription does not exist"));
        subscriptionRepository.deleteById(subscription.getId());
    }
}
