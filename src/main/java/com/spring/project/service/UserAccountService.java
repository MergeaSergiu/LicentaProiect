package com.spring.project.service;
import com.spring.project.dto.ReservationResponse;
import com.spring.project.dto.TrainingClassResponse;
import com.spring.project.model.FotballInsideReservation;


import java.util.List;

public interface UserAccountService {
    List<ReservationResponse> getAllClientReservation();

    List<TrainingClassResponse> getEnrollClasses();
}
