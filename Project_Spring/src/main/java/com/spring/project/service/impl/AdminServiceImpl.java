package com.spring.project.service.impl;

import com.spring.project.Exception.CustomExpiredJwtException;
import com.spring.project.dto.*;
import com.spring.project.email.EmailSender;
import com.spring.project.mapper.*;
import com.spring.project.model.*;
import com.spring.project.repository.*;
import com.spring.project.service.*;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
    private final ClientService clientService;
    private final EmailSender emailSender;
    private final ReservationService reservationService;
    private final SubscriptionService subscriptionService;
    private final TrainingClassService trainingClassService;
    private final ClientRepository clientRepository;
    private final EnrollmentTrainingClassService enrollmentTrainingClassService;
    private final ConfirmationTokenService confirmationTokenService;
    private final PasswordResetTokenService passwordResetTokenService;
    private final RoleRepsitory roleRepsitory;
    private final UserDataMapper userDataMapper;
    private final TrainerDataMapper trainerDataMapper;
    private final ReservationMapper reservationMapper;
    private final SubscriptionMapper subscriptionMapper;
    private final TrainingClassMapper trainingClassMapper;
    private final SubscriptionHistoryMapper subscriptionsHistoryMapper;
    private final EnrollmentTrainingClassRepository enrollmentTrainingClassRepository;
    private final TrainerCollaborationRepository trainerCollaborationRepository;
    private final TrainingClassRepository trainingClassRepository;

    private String loadEmailTemplateFromResource(String fileName) {
        try {
            Resource resource = new ClassPathResource(fileName);
            return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public List<UserDataResponse> getAllClients() {
            List<User> users = clientRepository.findAll();
              return users.stream()
                    .map(userDataMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public UserDataResponse getUserData(Long id) {
        User user = clientService.findClientById(id);
        return userDataMapper.convertToDto(user);
    }

    @Override
    public void deleteUser(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            User user = clientService.findClientById(id);
            if(user.getRole().getName().equals("TRAINER")){
                List<TrainingClass> trainingClasses = trainingClassService.getTrainingClassesForTrainer(id);
                if(trainingClasses != null){
                    for(TrainingClass trainingClass : trainingClasses){
                            deleteTrainingClass(trainingClass.getId());
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
        }
        confirmationTokenService.deleteByclient_Id(id);
        passwordResetTokenService.deleteByclient_Id(id);
        clientRepository.deleteById(id);
    }

    @Override
    public void updateUserRole(Long id, RoleRequest roleRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            Role role = roleRepsitory.findById(Long.valueOf(roleRequest.getId())).orElse(null);
            User user = clientService.findClientById(id);
            if(clientRepository.findById(user.getId()).get().getRole().getName().equals("USER") && !role.getName().equals("USER")){
                reservationService.deleteReservationsForUser(user.getId());
                enrollmentTrainingClassRepository.deleteAllByUser_id(user.getId());
                subscriptionHistoryRepository.deleteAllByUser_Id(id);
                trainerCollaborationRepository.deleteAllByUser_Id(id);
            }else if(clientRepository.findById(id).get().getRole().getName().equals("TRAINER") && !role.getName().equals("TRAINER")){
                List<TrainingClass> trainingClasses = trainingClassService.getTrainingClassesForTrainer(id);
                if(trainingClasses != null){
                    for(TrainingClass trainingClass : trainingClasses){
                        enrollmentTrainingClassRepository.deleteAllByTrainingClass_Id(trainingClass.getId());
                        deleteTrainingClass(trainingClass.getId());
                    }
                }
                trainerCollaborationRepository.deleteAllByTrainer_Id(id);
            }
            user.setRole(role);
            clientRepository.save(user);
        }
    }

    @Override
    public List<TrainerResponse> getAllTrainers() {
        List<User> trainers = clientRepository.getAllTrainers();
        return trainers.stream()
                .map(trainerDataMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<ReservationResponse> getAllReservations() {
            List<Reservation> reservations = reservationService.getAllReservations();
            return reservations.stream()
                    .map(reservationMapper::convertToDto).collect(Collectors.toList());

    }

    @Override
    public List<SubscriptionResponse> getAllSubscriptions() {
        List<Subscription> subscriptions = subscriptionService.getAllSubscriptionPlans();
        return subscriptions.stream()
                .map(subscriptionMapper::convertToDto).collect(Collectors.toList());
    }

    public void createSubscription(CreateSubscriptionRequest createSubscriptionRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            Subscription foundsubscription = subscriptionService.findBySubscriptionName(createSubscriptionRequest.getSubscriptionName());
            if (foundsubscription == null) {
                Subscription subscription = subscriptionMapper.convertFromDto(createSubscriptionRequest);
                subscriptionService.saveSubscription(subscription);
            }else{
                throw new EntityExistsException("There is already a subscription with this name");
            }
        }
    }

    @Override
    public void updateSubscription(Long id, CreateSubscriptionRequest subscriptionRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            Subscription subscription = subscriptionService.findById(id).orElse(null);
            if (subscription != null) {
                if (subscriptionRequest.getSubscriptionName().equals(subscriptionService.findById(id).get().getSubscriptionName()) || subscriptionService.findBySubscriptionName(subscriptionRequest.getSubscriptionName()) == null) {
                    subscription.setSubscriptionName(subscriptionRequest.getSubscriptionName());
                    subscription.setSubscriptionPrice(subscriptionRequest.getSubscriptionPrice());
                    subscription.setSubscriptionTime(subscriptionRequest.getSubscriptionTime());
                    subscription.setSubscriptionDescription(subscriptionRequest.getSubscriptionDescription());
                    subscriptionService.saveSubscription(subscription);
                } else {
                    throw new EntityExistsException("There is already a subscription with this name");
                }
            } else {
                throw new EntityNotFoundException("There is no subscription with this name");
            }
        }
    }

    @Override
    public SubscriptionResponse getSubscriptionById(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            Subscription subscription = subscriptionService.findById(id).orElse(null);
            if(subscription != null){
                return subscriptionMapper.convertToDto(subscription);
            }
        }
        throw new CustomExpiredJwtException("Session expired");
    }

    @Override
    public void deleteSubscription(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            subscriptionService.deleteSubscription(id);
        }
    }

    @Override
    public List<TrainingClassResponse> getAllTrainingClasses(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            List<TrainingClass> trainingClasses = trainingClassService.getTrainingClasses();
            return trainingClasses.stream()
                    .map(trainingClassMapper::convertToDto)
                            .collect(Collectors.toList());

        }else {
            throw new CustomExpiredJwtException("Session expired");
        }
    }

    @Override
    public TrainingClassResponse getTrainingClass(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            TrainingClass trainingClass = trainingClassService.findById(id);
            return trainingClassMapper.convertToDto(trainingClass);
        }else {
            throw new CustomExpiredJwtException("Session expired");
        }
    }

    @Override
    public TrainingClass createTrainingClass(TrainingClassRequest classRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated() && clientService.findClientById(Long.valueOf(classRequest.getTrainerId())) != null) {
                    User trainer = clientService.findClientById(Long.valueOf(classRequest.getTrainerId()));
                    TrainingClass trainingClass = trainingClassMapper.convertFromDto(classRequest, trainer);
                    trainingClassService.createTrainingClass(trainingClass);
                    String emailTemplate = loadEmailTemplateFromResource("trainingClassCreated.html");
                    emailTemplate = emailTemplate.replace("${email}", authentication.getName());
                    emailTemplate = emailTemplate.replace("${trainingClass}", classRequest.getClassName());
                    emailSender.send(authentication.getName(), emailTemplate, "Training class was created");
                    return trainingClass;
        } else {
            throw new CustomExpiredJwtException("Session has expired");
        }
    }

    @Override
    public void updateTrainingClass(Long id, TrainingClassRequest trainingClassRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            TrainingClass trainingClass = trainingClassService.findById(id);
            if(trainingClass != null){
                if(trainingClassService.getTrainingClassByName(trainingClassRequest.getClassName()) == null || trainingClassRequest.getClassName().equals(trainingClassService.findById(trainingClass.getId()).getClassName())){
                    trainingClass.setClassName(trainingClassRequest.getClassName());
                    trainingClass.setIntensity(trainingClassRequest.getIntensity());
                    trainingClass.setStartTime(trainingClass.getStartTime());
                    trainingClass.setDuration(trainingClassRequest.getDuration());
                    trainingClass.setLocalDate(trainingClassRequest.getLocalDate());
                    trainingClass.setTrainer(clientService.findClientById(Long.valueOf(trainingClassRequest.getTrainerId())));
                    trainingClassService.createTrainingClass(trainingClass);
                }else {
                    throw new EntityExistsException("There is a training class with this name");
                }
            }else{
                throw new EntityNotFoundException("Training class could not be updated");
            }
        }
    }

    @Override
    public void deleteTrainingClass(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            enrollmentTrainingClassService.deleteAllEnrollsForTrainingClass(id);
            trainingClassService.deleteTrainingClass(id);
         }
    }

    @Override
    public List<UserSubscriptionsDataResponse> getUserSubscriptionsData(Long id) {
        List<UserSubscriptionsDataResponse> responseData = new ArrayList<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            User user = clientRepository.findById(id).orElse(null);
            if(user != null) {
                List<SubscriptionsHistory> subscriptionsHistoryListForUser = subscriptionHistoryRepository.findByUser_IdOrderBySubscriptionEndTimeAsc(id);
                return subscriptionsHistoryListForUser.stream()
                        .map(subscriptionsHistoryMapper::convertToDto).collect(Collectors.toList());

            }
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
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
        }else{
            throw new CustomExpiredJwtException("Session has expired");
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
                    String emailTemplate = loadEmailTemplateFromResource("paymentResponse.html");
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