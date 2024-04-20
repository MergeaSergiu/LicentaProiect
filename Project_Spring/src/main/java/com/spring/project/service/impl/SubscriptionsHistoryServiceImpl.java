package com.spring.project.service.impl;

import com.spring.project.dto.UserSubscriptionRequest;
import com.spring.project.dto.UserSubscriptionsDataResponse;
import com.spring.project.mapper.SubscriptionHistoryMapper;
import com.spring.project.model.Subscription;
import com.spring.project.model.SubscriptionsHistory;
import com.spring.project.model.User;
import com.spring.project.repository.ClientRepository;
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

    private final ClientRepository clientRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionHistoryRepository subscriptionHistoryRepository;
    private final SubscriptionHistoryMapper subscriptionHistoryMapper;
    private final UtilMethods utilMethods;
    private final EmailSenderImpl emailService;

    @Override
    public SubscriptionsHistory addSubscriptionForUser(UserSubscriptionRequest userSubscriptionRequest) {
        User user = clientRepository.findById(Long.valueOf(userSubscriptionRequest.getUserId())).orElse(null);
        if(user == null){
            throw new EntityNotFoundException("User does not exist");
        }
        Subscription subscription = subscriptionRepository.findById(Long.valueOf(userSubscriptionRequest.getSubscriptionId())).orElse(null);
        if(subscription ==null){
            throw new EntityExistsException("Subscription does not exist");
        }
        SubscriptionsHistory activeSubscription = subscriptionHistoryRepository.findActiveSubscriptionForUser(user.getId(), LocalDate.now());
        if(activeSubscription != null){
            throw new EntityExistsException("User already has an active subscription");
        }
            SubscriptionsHistory subscriptionsHistory = subscriptionHistoryMapper.convertFromDto(user,subscription);
            subscriptionHistoryRepository.save(subscriptionsHistory);
            return subscriptionsHistory;
    }

    @Override
    public List<UserSubscriptionsDataResponse> getLoggedInUserSubscriptions(String authorization) {
        String username = utilMethods.extractUsernameFromAuthorizationHeader(authorization);
        User user = clientRepository.findByEmail(username).orElse(null);
        if(user == null){
            throw new EntityNotFoundException("User does not exist");
        }
            List<SubscriptionsHistory> subscriptionsHistoryListForUser = subscriptionHistoryRepository.findByUser_IdOrderBySubscriptionEndTimeAsc(user.getId());
            return subscriptionsHistoryListForUser.stream()
                    .map(subscriptionHistoryMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<UserSubscriptionsDataResponse> getUserSubscriptions(Long id) {
        User user = clientRepository.findById(id).orElse(null);
        if(user == null){
            throw new EntityNotFoundException("User does not exist");
        }
        List<SubscriptionsHistory> subscriptionsHistoryListForUser = subscriptionHistoryRepository.findByUser_IdOrderBySubscriptionEndTimeAsc(id);
        return subscriptionsHistoryListForUser.stream()
                    .map(subscriptionHistoryMapper::convertToDto).collect(Collectors.toList());

    }

    @Override
    public void addSubscriptionForUserByCard(Long subscriptionId, String authorization) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String username = utilMethods.extractUsernameFromAuthorizationHeader(authorization);
            User user = clientRepository.findByEmail(username).orElse(null);
            if(user == null){
                throw new EntityNotFoundException("User does not exist");
            }
            Subscription subscription = subscriptionRepository.findById(subscriptionId).orElse(null);
            if(subscription == null){
                throw new EntityNotFoundException("Subscription does not exist");
            }
            SubscriptionsHistory activeSubscription = subscriptionHistoryRepository.findActiveSubscriptionForUser(user.getId(), LocalDate.now());
            if(activeSubscription != null){
                throw new EntityExistsException("User already has a active subscription");
            }
                SubscriptionsHistory subscriptionsHistory = subscriptionHistoryMapper.convertFromDto(user,subscription);
                subscriptionHistoryRepository.save(subscriptionsHistory);
                String emailTemplate = utilMethods.loadEmailTemplateFromResource("paymentResponse.html");
                emailTemplate = emailTemplate.replace("${user}", user.getFirstName()+" " + user.getLastName());
                emailTemplate = emailTemplate.replace("${subscription}", subscription.getSubscriptionName());
                emailTemplate = emailTemplate.replace("${price}", subscription.getSubscriptionPrice().toString());
                emailTemplate = emailTemplate.replace("${date}", LocalDateTime.now().format(formatter));
                emailService.send(username, emailTemplate, "Payment billing");
    }
}
