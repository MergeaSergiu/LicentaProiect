package com.spring.project.service.impl;

import com.spring.project.Exception.ClientNotFoundException;
import com.spring.project.dto.*;
import com.spring.project.email.EmailSender;
import com.spring.project.mapper.*;
import com.spring.project.model.*;
import com.spring.project.repository.*;
import com.spring.project.service.*;
import com.spring.project.util.UtilMethods;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final SubscriptionHistoryRepository subscriptionHistoryRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final EmailSender emailSender;
    private final ReservationService reservationService;
    private final TrainingClassService trainingClassService;
    private final ClientRepository clientRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final PasswordResetTokenService passwordResetTokenService;
    private final RoleRepository roleRepository;
    private final UserDataMapper userDataMapper;
    private final TrainerDataMapper trainerDataMapper;
    private final SubscriptionHistoryMapper subscriptionsHistoryMapper;
    private final EnrollmentTrainingClassRepository enrollmentTrainingClassRepository;
    private final TrainerCollaborationRepository trainerCollaborationRepository;
    private final TrainingClassRepository trainingClassRepository;
    private final UtilMethods utilMethods;


    @Override
    public List<UserDataResponse> getAllClients() {
            List<User> users = clientRepository.findAll();
              return users.stream()
                    .map(userDataMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public UserDataResponse getUserData(Long id) {
        User user = clientRepository.findById(id).orElse(null);
        if(user == null){
            throw new ClientNotFoundException("User does not exist");
        }
        return userDataMapper.convertToDto(user);
    }

    @Override
    public void deleteUser(Long id) {
            User user = clientRepository.findById(id).orElse(null);
            if(user == null){
                throw new ClientNotFoundException("User does not exist");
            }
            if(user.getRole().getName().equals("TRAINER")){
                List<TrainingClass> trainingClasses = trainingClassService.getTrainingClassesForTrainer(id);
                if(trainingClasses != null){
                    for(TrainingClass trainingClass : trainingClasses){
                            trainingClassRepository.deleteById(trainingClass.getId());
                            enrollmentTrainingClassRepository.deleteAllByTrainingClass_Id(id);
                            trainerCollaborationRepository.deleteAllByTrainer_Id(id);
                    }
                }
            }else if(user.getRole().getName().equals("USER")) {
                reservationService.deleteReservationsForUser(id);
                enrollmentTrainingClassRepository.deleteAllByUser_id(id);
                subscriptionHistoryRepository.deleteAllByUser_Id(id);
                trainerCollaborationRepository.deleteAllByUser_Id(id);
            }
        confirmationTokenService.deleteByclient_Id(id);
        passwordResetTokenService.deleteByclient_Id(id);
        clientRepository.deleteById(id);
    }

    @Override
    public void updateUserRole(Long id, RoleRequest roleRequest) {
            Role role = roleRepository.findById(Long.valueOf(roleRequest.getId())).orElse(null);
            if(role == null){
                throw new EntityNotFoundException("Role does not exist");
            }
            User user = clientRepository.findById(id).orElse(null);
            if(user == null){
                throw new ClientNotFoundException("User does not exist");
            }
            if(user.getRole().getName().equals("USER") && !role.getName().equals("USER")){
                reservationService.deleteReservationsForUser(user.getId());
                enrollmentTrainingClassRepository.deleteAllByUser_id(user.getId());
                subscriptionHistoryRepository.deleteAllByUser_Id(id);
                trainerCollaborationRepository.deleteAllByUser_Id(id);
            }else if(user.getRole().getName().equals("TRAINER") && !role.getName().equals("TRAINER")){
                List<TrainingClass> trainingClasses = trainingClassService.getTrainingClassesForTrainer(id);
                if(trainingClasses != null){
                    for(TrainingClass trainingClass : trainingClasses){
                        enrollmentTrainingClassRepository.deleteAllByTrainingClass_Id(trainingClass.getId());
                        trainingClassRepository.deleteById(trainingClass.getId());
                    }
                }
                trainerCollaborationRepository.deleteAllByTrainer_Id(id);
            }
            user.setRole(role);
            clientRepository.save(user);
    }

    @Override
    public List<TrainerResponse> getAllTrainers() {
        List<User> trainers = clientRepository.getAllTrainers();
        return trainers.stream()
                .map(trainerDataMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<UserSubscriptionsDataResponse> getUserSubscriptionsData(Long id) {
        List<UserSubscriptionsDataResponse> responseData = new ArrayList<>();
            User user = clientRepository.findById(id).orElse(null);
            if(user != null) {
                List<SubscriptionsHistory> subscriptionsHistoryListForUser = subscriptionHistoryRepository.findByUser_IdOrderBySubscriptionEndTimeAsc(id);
                return subscriptionsHistoryListForUser.stream()
                        .map(subscriptionsHistoryMapper::convertToDto).collect(Collectors.toList());

            }
        return responseData;
    }

    @Override
    public List<UserSubscriptionsDataResponse> getUserSubscriptionsData() {
        List<UserSubscriptionsDataResponse> responseData = new ArrayList<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            User user = clientRepository.findByEmail(authentication.getName()).orElse(null);
            if(user != null) {
                List<SubscriptionsHistory> subscriptionsHistoryListForUser = subscriptionHistoryRepository.findByUser_IdOrderBySubscriptionEndTimeAsc(user.getId());
                return subscriptionsHistoryListForUser.stream()
                        .map(subscriptionsHistoryMapper::convertToDto).collect(Collectors.toList());
            }
        }
        return responseData;
    }

    @Override
    public SubscriptionsHistory addSubscriptionForUser(UserSubscriptionRequest userSubscriptionRequest) {
            User user = clientRepository.findById(Long.valueOf(userSubscriptionRequest.getUserId())).orElse(null);
            Subscription subscription = subscriptionRepository.findById(Long.valueOf(userSubscriptionRequest.getSubscriptionId())).orElse(null);
            SubscriptionsHistory activeSubscription = subscriptionHistoryRepository.findActiveSubscriptionForUser(user.getId(), LocalDate.now());
            if(activeSubscription == null && subscription != null){
                SubscriptionsHistory subscriptionsHistory = subscriptionsHistoryMapper.convertFromDto(user,subscription);
                subscriptionHistoryRepository.save(subscriptionsHistory);
                return subscriptionsHistory;
            }else{
                throw new EntityExistsException("User already has a active subscription");
            }
    }

    @Override
    public void addSubscriptionForUserByCard(Long subscriptionId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            User user = clientRepository.findByEmail(authentication.getName()).orElse(null);
            Subscription subscription = subscriptionRepository.findById(subscriptionId).orElse(null);
            SubscriptionsHistory activeSubscription = subscriptionHistoryRepository.findActiveSubscriptionForUser(user.getId(), LocalDate.now());
            if(activeSubscription == null && subscription != null){
                    SubscriptionsHistory subscriptionsHistory = subscriptionsHistoryMapper.convertFromDto(user,subscription);
                    subscriptionHistoryRepository.save(subscriptionsHistory);
                    String emailTemplate = utilMethods.loadEmailTemplateFromResource("paymentResponse.html");
                    emailTemplate = emailTemplate.replace("${email}", authentication.getName());
                    emailTemplate = emailTemplate.replace("${subscription}", subscription.getSubscriptionName());
                    emailTemplate = emailTemplate.replace("${price}", subscription.getSubscriptionPrice().toString());
                    emailTemplate = emailTemplate.replace("${date}", LocalDateTime.now().format(formatter));
                    emailSender.send(authentication.getName(), emailTemplate, "Payment billing");
            }else{
                throw new EntityExistsException("User already has a active subscription");
            }
        }
    }


}