package com.spring.project.service.impl;

import com.spring.project.Exception.ClientNotFoundException;
import com.spring.project.Exception.CustomExpiredJwtException;
import com.spring.project.dto.*;
import com.spring.project.mapper.ReservationMapper;
import com.spring.project.mapper.TrainingClassMapper;
import com.spring.project.mapper.UserDataMapper;
import com.spring.project.model.*;
import com.spring.project.repository.ClientRepository;
import com.spring.project.repository.SubscriptionHistoryRepository;
import com.spring.project.service.EnrollmentTrainingClassService;
import com.spring.project.service.ReservationService;
import com.spring.project.service.TrainingClassService;
import com.spring.project.service.UserAccountService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {

    private final ReservationService reservationService;
    private final EnrollmentTrainingClassService enrollmentTrainingClassService;
    private final ClientService clientService;
    private final TrainingClassService trainingClassService;
    private final ClientRepository clientRepository;
    private final ReservationMapper reservationMapper;
    private final TrainingClassMapper trainingClassMapper;
    private final UserDataMapper userDataMapper;
    private final SubscriptionHistoryRepository subscriptionHistoryRepository;

    @Override
    public List<ReservationResponse> getAllClientReservations() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(authentication.isAuthenticated()) {
                Client user = clientService.findClientByEmail(authentication.getName());
                List<Reservation> reservations = reservationService.getAllClientReservations(user.getId());
                    return reservations.stream()
                            .map(courtReservation -> reservationMapper.convertToDto(courtReservation)).collect(Collectors.toList());
            }
        return new ArrayList<>();
    }

    @Override
    public List<TrainingClassResponse> getEnrollClasses() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Client client = clientService.findClientByEmail(authentication.getName());
            List<EnrollmentTrainingClass> enrollmentTrainingClasses = enrollmentTrainingClassService.getClassesByUserId(client.getId());
            if (enrollmentTrainingClasses != null) {
                List<TrainingClassResponse> enrollClassResponses = new ArrayList<>();
                for(EnrollmentTrainingClass enrollmentTrainingClass : enrollmentTrainingClasses){
                    TrainingClass trainingClass = trainingClassService.findById(enrollmentTrainingClass.getTrainingClass().getId());
                    enrollClassResponses.add(trainingClassMapper.convertToDto(trainingClass));
                }
                return enrollClassResponses;
            }else{
                return null;
            }
    }

    @Override
    public void updateUserProfile(UpdateUserRequest updateUserRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            Client user = clientService.findClientByEmail(authentication.getName());
            user.setFirstName(updateUserRequest.getFirstName());
            user.setLastName(updateUserRequest.getLastName());
            clientRepository.save(user);
        }
    }

    @Override
    public UserDataResponse getUserProfileData() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            Client user = clientService.findClientByEmail(authentication.getName());
            return userDataMapper.convertToDto(user);
        }
        throw new EntityNotFoundException("User does not exist");
    }

    @Override
    public boolean getUserActiveSubscriptions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            Client user = clientService.findClientByEmail(authentication.getName());
            if(user != null){
                SubscriptionsHistory activeSubscription = subscriptionHistoryRepository.findActiveSubscriptionForUser(user.getId(), LocalDate.now());
                if(activeSubscription == null){
                    return false;
                }else{
                    return true;
                }
            }
        }
        return false;
    }
}
