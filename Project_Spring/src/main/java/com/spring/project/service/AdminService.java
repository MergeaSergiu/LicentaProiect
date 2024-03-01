package com.spring.project.service;

import com.spring.project.dto.*;

import java.util.List;
import java.util.Map;

public interface AdminService {
     TrainerResponse createTrainer(TrainerRequest trainerRequest);

     List<ClientResponse> getAllClients();

     List<TrainerResponse> getAllTrainers();

     List<ReservationResponse> getAllReservations();

     List<SubscriptionResponse> getAllSubscriptions();

     void createSubscription(CreateSubscriptionRequest createSubscriptionRequest);

     void updateSubscription(Integer id, CreateSubscriptionRequest subscriptionRequest);

     SubscriptionResponse getSubscriptionById(Integer id);

     void deleteSubscription(Integer id);

     List<TrainingClassResponse> getAllTrainingClasses();

     TrainingClassResponse getTrainingClass(Integer id);

     void createTrainingClass(TrainingClassRequest classRequest);

    void updateTrainingClass(Integer id, TrainingClassRequest trainingClassRequest);

    void deleteTrainingClass(Integer id);
}
