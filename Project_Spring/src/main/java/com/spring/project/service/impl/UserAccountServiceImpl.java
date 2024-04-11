package com.spring.project.service.impl;

import com.spring.project.dto.*;
import com.spring.project.mapper.UserDataMapper;
import com.spring.project.model.*;
import com.spring.project.repository.ClientRepository;
import com.spring.project.repository.SubscriptionHistoryRepository;
import com.spring.project.service.UserAccountService;
import com.spring.project.util.UtilMethods;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {

    private final ClientRepository clientRepository;
    private final UserDataMapper userDataMapper;
    private final SubscriptionHistoryRepository subscriptionHistoryRepository;
    private final UtilMethods utilMethods;


    @Override
    public void updateUserProfile(UpdateUserRequest updateUserRequest, String authorization) {
            String username = utilMethods.extractUsernameFromAuthorizationHeader(authorization);
            User user = clientRepository.findByEmail(username).orElse(null);
            if(user == null){
                throw new EntityNotFoundException("User does not exist");
            }
            user.setFirstName(updateUserRequest.getFirstName());
            user.setLastName(updateUserRequest.getLastName());
            clientRepository.save(user);
    }

    @Override
    public UserDataResponse getUserProfileData(String authorization) {
            String username = utilMethods.extractUsernameFromAuthorizationHeader(authorization);
            User user = clientRepository.findByEmail(username).orElse(null);
            if(user == null) {
                throw new EntityNotFoundException("User does not exist");
            }
            return userDataMapper.convertToDto(user);
    }

    @Override
    public boolean getUserActiveSubscriptions(String authorization) {
            String username = utilMethods.extractUsernameFromAuthorizationHeader(authorization);
            User user = clientRepository.findByEmail(username).orElse(null);
            if(user == null) {
                throw new EntityNotFoundException("User does not exist");
            }
            SubscriptionsHistory activeSubscription = subscriptionHistoryRepository.findActiveSubscriptionForUser(user.getId(), LocalDate.now());
            return activeSubscription != null;
    }
}
