package com.spring.project.service.impl;

import com.spring.project.Exception.CustomExpiredJwtException;
import com.spring.project.dto.ReservationResponse;
import com.spring.project.dto.TrainingClassResponse;
import com.spring.project.model.Client;
import com.spring.project.model.EnrollmentTrainingClass;
import com.spring.project.model.FotballInsideReservation;
import com.spring.project.model.TrainingClass;
import com.spring.project.service.EnrollmentTrainingClassService;
import com.spring.project.service.ReservationService;
import com.spring.project.service.TrainingClassService;
import com.spring.project.service.UserAccountService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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

    @Override
    public List<ReservationResponse> getAllClientReservation() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            List<FotballInsideReservation> fotballInsideReservations = reservationService.getAllClientReservation(authentication.getName());
            if(fotballInsideReservations != null) {
                return fotballInsideReservations.stream()
                        .map(fotballInsideReservation -> ReservationResponse.builder()
                                .hourSchedule(fotballInsideReservation.getHourSchedule())
                                .localDate(fotballInsideReservation.getLocalDate())
                                .email(fotballInsideReservation.getEmail())
                                .build())
                        .collect(Collectors.toList());
            }else {
                throw new EntityNotFoundException("You do not have any reservation yet");
            }
        }
        return null;
    }

    @Override
    public List<TrainingClassResponse> getEnrollClasses() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()) {
            Client client = clientService.findClientByEmail(authentication.getName());
            List<EnrollmentTrainingClass> enrollmentTrainingClasses = enrollmentTrainingClassService.getClassesByUserId(client.getId());
            if (enrollmentTrainingClasses != null) {
                List<TrainingClassResponse> enrollClassResponses = new ArrayList<>();
                for(EnrollmentTrainingClass enrollmentTrainingClass : enrollmentTrainingClasses){
                    TrainingClass trainingClass = trainingClassService.findById(enrollmentTrainingClass.getTrainingClass().getId());
                    enrollClassResponses.add(TrainingClassResponse.builder()
                            .className(trainingClass.getClassName())
                                    .intensity(trainingClass.getIntensity())
                                    .duration(trainingClass.getDuration())
                            .localDate(trainingClass.getLocalDate())
                                    .trainerEmail(trainingClass.getTrainer().getEmail())
                            .build());
                }
                return enrollClassResponses;
            }else{
                return null;
            }
        }
        return null;
    }
}
