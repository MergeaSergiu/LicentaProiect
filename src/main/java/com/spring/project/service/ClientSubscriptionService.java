package com.spring.project.service;

import com.spring.project.Exception.CustomExpiredJwtException;
import com.spring.project.Exception.InvalidCredentialsException;
import com.spring.project.model.Subscription;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ClientSubscriptionService {

    private final SubscriptionService subscriptionService;

    public List<Subscription> getSubscriptions() {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication.getName() != null && authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CLIENT"))) {
                return subscriptionService.getAllSubscriptionPlans();
            } else {
                throw new CustomExpiredJwtException("Session expired");
            }
    }
}
