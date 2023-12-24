package com.spring.project.service;

import com.spring.project.Exception.CustomExpiredJwtException;
import com.spring.project.Exception.InvalidCredentialsException;
import com.spring.project.model.FotballInsideReservation;
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
public class UserAccountService {

    private final ReservationService reservationService;

    public List<FotballInsideReservation> getAllClientReservation() {

//        final String authHeader = request.getHeader("Authorization");
//        final String jwt;
//        final String clientEmail;
//        final String clientRole;
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            throw new InvalidCredentialsException("Admin is not loged In");
//        }
//        jwt = authHeader.substring(7);
//        clientEmail = jwtService.extractClientUsername(jwt);
//        clientRole = jwtService.extractClientRole(jwt);
//        if (clientEmail != null && clientRole.equals("CLIENT") && SecurityContextHolder.getContext().getAuthentication() != null) {
//            UserDetails clientDetails = this.clientDetailsService.loadUserByUsername(clientEmail);
//            if (jwtService.isTokenValid(jwt, clientDetails)) {
//                return reservationService.getAllClientReservation(clientEmail);
//            }
//        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getName() != null && authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CLIENT"))) {
            return reservationService.getAllClientReservation(authentication.getName());
        } else {
            throw new CustomExpiredJwtException("Session expired");
        }
    }
}
