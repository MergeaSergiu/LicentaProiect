package com.spring.project.service;
import com.spring.project.dto.*;
import com.spring.project.model.SubscriptionsHistory;


import java.util.List;

public interface UserAccountService {
    List<ReservationResponse> getAllClientReservations();

    List<TrainingClassResponse> getEnrollClasses();

    void updateUserProfile(UpdateUserRequest updateUserRequest);

    UserDataResponse getUserProfileData();

    boolean getUserActiveSubscriptions();
}
