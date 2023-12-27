package com.spring.project.service;

import com.spring.project.dto.*;

import java.util.List;
import java.util.Map;

public interface AdminService {
     TrainerResponse createTrainer(TrainerRequest trainerRequest);

     List<ClientResponse> getAllClients();

     List<ReservationResponse> getAllReservations();

     void createSubscription(CreateSubscriptionRequest createSubscriptionRequest);

     void updateSubscription(Integer id, Map<String, Object> fields);

     void deleteSubscription(Integer id);

     void createTrainingClass(CreateClassRequest classRequest);

    void updateTrainingClass(Integer id, Map<String, Object> fields);

    void deleteTrainingClass(Integer id);
}
