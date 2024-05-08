package com.spring.project.service.impl;

import com.spring.project.dto.UserSubscriptionRequest;
import com.spring.project.dto.UserSubscriptionsDataResponse;
import com.spring.project.mapper.SubscriptionHistoryMapper;
import com.spring.project.model.Subscription;
import com.spring.project.model.SubscriptionsHistory;
import com.spring.project.model.User;
import com.spring.project.repository.UserRepository;
import com.spring.project.repository.SubscriptionHistoryRepository;
import com.spring.project.repository.SubscriptionRepository;
import com.spring.project.service.SubscriptionsHistoryService;
import com.spring.project.util.UtilMethods;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SubscriptionsHistoryServiceImpl implements SubscriptionsHistoryService {

    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionHistoryRepository subscriptionHistoryRepository;
    private final SubscriptionHistoryMapper subscriptionHistoryMapper;
    private final UtilMethods utilMethods;
    private final EmailSenderImpl emailService;

    @Override
    public SubscriptionsHistory addSubscriptionForUser(UserSubscriptionRequest userSubscriptionRequest) {
        User user = userRepository.findById(Long.valueOf(userSubscriptionRequest.getUserId())).orElseThrow(() -> new EntityNotFoundException("User does not exist"));
        if (!user.getEnabled()) {
            throw new EntityNotFoundException("This account is not enabled");
        }
        if (user.getRole().getName().equals("TRAINER")) {
            throw new EntityNotFoundException("Trainers already have access to all subscriptions");
        }

        Subscription subscription = subscriptionRepository.findById(Long.valueOf(userSubscriptionRequest.getSubscriptionId())).orElseThrow(() -> new EntityExistsException("Subscription does not exist"));

        SubscriptionsHistory activeSubscription = subscriptionHistoryRepository.findActiveSubscriptionForUser(user.getId(), LocalDate.now());
        if (activeSubscription != null) {
            throw new EntityExistsException("User already has an active subscription");
        }
        SubscriptionsHistory subscriptionsHistory = subscriptionHistoryMapper.convertFromDto(user, subscription);
        subscriptionHistoryRepository.save(subscriptionsHistory);
        return subscriptionsHistory;
    }

    @Override
    public List<UserSubscriptionsDataResponse> getLoggedInUserSubscriptions(String authorization) {
        User user = utilMethods.extractUsernameFromAuthorizationHeader(authorization);
        List<SubscriptionsHistory> subscriptionsHistoryListForUser = subscriptionHistoryRepository.findByUser_IdOrderBySubscriptionEndTimeAsc(user.getId());
        return subscriptionsHistoryListForUser.stream()
                .map(subscriptionHistoryMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<UserSubscriptionsDataResponse> getUserSubscriptions(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User does not exist"));
        List<SubscriptionsHistory> subscriptionsHistoryListForUser = subscriptionHistoryRepository.findByUser_IdOrderBySubscriptionEndTimeAsc(user.getId());
        return subscriptionsHistoryListForUser.stream()
                .map(subscriptionHistoryMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public void addSubscriptionForUserByCard(Long subscriptionId, String authorization) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        User user = utilMethods.extractUsernameFromAuthorizationHeader(authorization);

        if (user.getRole().getName().equals("TRAINER")) {
            throw new EntityNotFoundException("Trainers already have access to all subscriptions");
        }
        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElseThrow(() -> new EntityNotFoundException("Subscription does not exist"));

        SubscriptionsHistory activeSubscription = subscriptionHistoryRepository.findActiveSubscriptionForUser(user.getId(), LocalDate.now());
        if (activeSubscription != null) {
            throw new EntityExistsException("User already has a active subscription");
        }
        SubscriptionsHistory subscriptionsHistory = subscriptionHistoryMapper.convertFromDto(user, subscription);
        subscriptionHistoryRepository.save(subscriptionsHistory);
        String emailTemplate = utilMethods.loadEmailTemplateFromResource("paymentResponse.html");
        emailTemplate = emailTemplate.replace("${user}", user.getFirstName() + " " + user.getLastName());
        emailTemplate = emailTemplate.replace("${subscription}", subscription.getSubscriptionName());
        emailTemplate = emailTemplate.replace("${price}", subscription.getSubscriptionPrice().toString());
        emailTemplate = emailTemplate.replace("${date}", LocalDateTime.now().format(formatter));
        emailService.send(user.getEmail(), emailTemplate, "Payment billing");
    }
}
