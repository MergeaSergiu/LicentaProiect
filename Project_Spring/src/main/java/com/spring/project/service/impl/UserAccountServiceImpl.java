package com.spring.project.service.impl;

import com.spring.project.dto.*;
import com.spring.project.mapper.UserDataMapper;
import com.spring.project.model.*;
import com.spring.project.repository.UserRepository;
import com.spring.project.repository.SubscriptionHistoryRepository;
import com.spring.project.service.UserAccountService;
import com.spring.project.util.UtilMethods;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {

    private final UserRepository userRepository;
    private final UserDataMapper userDataMapper;
    private final SubscriptionHistoryRepository subscriptionHistoryRepository;
    private final UtilMethods utilMethods;


    @Override
    public void updateUserProfile(UpdateUserRequest updateUserRequest, String authorization) {
            String username = utilMethods.extractUsernameFromAuthorizationHeader(authorization);
            User user = userRepository.findByEmail(username).orElse(null);
            if(user == null){
                throw new EntityNotFoundException("User does not exist");
            }
            user.setFirstName(updateUserRequest.getFirstName());
            user.setLastName(updateUserRequest.getLastName());
            userRepository.save(user);
    }

    @Override
    public UserDataResponse getUserProfileData(String authorization) {
            String username = utilMethods.extractUsernameFromAuthorizationHeader(authorization);
            User user = userRepository.findByEmail(username).orElse(null);
            if(user == null) {
                throw new EntityNotFoundException("User does not exist");
            }
            return userDataMapper.convertToDto(user);
    }

    @Override
    public boolean getUserActiveSubscriptions(String authorization) {
            String username = utilMethods.extractUsernameFromAuthorizationHeader(authorization);
            User user = userRepository.findByEmail(username).orElse(null);
            if(user == null) {
                throw new EntityNotFoundException("User does not exist");
            }
            SubscriptionsHistory activeSubscription = subscriptionHistoryRepository.findActiveSubscriptionForUser(user.getId(), LocalDate.now());
            return activeSubscription != null;
    }
}
