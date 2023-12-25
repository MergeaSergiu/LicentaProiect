package com.spring.project.service;

import com.spring.project.Exception.CustomExpiredJwtException;
import com.spring.project.model.FotballInsideReservation;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserAccountService {

    private final ReservationService reservationService;

    public List<FotballInsideReservation> getAllClientReservation() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getName() != null && authentication.isAuthenticated()) {
            return reservationService.getAllClientReservation(authentication.getName());
        } else {
            throw new CustomExpiredJwtException("Session expired");
        }
    }
}
