package com.spring.project.service;
import com.spring.project.dto.ReservationResponse;
import com.spring.project.dto.TrainingClassResponse;
import com.spring.project.dto.UpdateUserRequest;
import com.spring.project.dto.UserDataResponse;


import java.util.List;

public interface UserAccountService {
    List<ReservationResponse> getAllClientReservations();

    List<TrainingClassResponse> getEnrollClasses();

    void updateUserProfile(UpdateUserRequest updateUserRequest);

    UserDataResponse getUserProfileData();
}
