package com.spring.project.service;

import com.spring.project.Exception.CustomExpiredJwtException;
import com.spring.project.model.Subscription;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ClientSubscriptionService {

    private final SubscriptionService subscriptionService;

    public List<Subscription> getSubscriptions() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication.isAuthenticated()) {
                return subscriptionService.getAllSubscriptionPlans();
            } else {
                throw new CustomExpiredJwtException("Session expired");
            }
    }
}
