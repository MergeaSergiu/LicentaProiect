package com.spring.project.service;

import com.spring.project.dto.*;
import com.spring.project.model.Subscription;
import com.spring.project.model.SubscriptionsHistory;
import com.spring.project.model.TrainingClass;

import java.util.List;

public interface AdminService {

     List<UserDataResponse> getAllClients();

     UserDataResponse getUserData(Long id);

     void deleteUser(Long id);

     void updateUserRole(Long id, RoleRequest roleRequest);

     List<TrainerResponse> getAllTrainers();

     List<ReservationResponse> getAllReservations();

     List<SubscriptionResponse> getAllSubscriptions();

     void createSubscription(CreateSubscriptionRequest createSubscriptionRequest);

     void updateSubscription(Long id, CreateSubscriptionRequest subscriptionRequest);

     SubscriptionResponse getSubscriptionById(Long id);

     void deleteSubscription(Long id);

     List<TrainingClassResponse> getAllTrainingClasses();

     TrainingClassResponse getTrainingClass(Long id);

     TrainingClass createTrainingClass(TrainingClassRequest classRequest);

    void updateTrainingClass(Long id, TrainingClassRequest trainingClassRequest);

    void deleteTrainingClass(Long id);

     List<UserSubscriptionsDataResponse> getUserSubscriptionsData(Long id);

     List<UserSubscriptionsDataResponse> getUserSubscriptionsData();

     SubscriptionsHistory addSubscriptionForUser(UserSubscriptionRequest userSubscriptionRequest);

    void addSubscriptionForUserByCard(Long subscriptionId);
}
