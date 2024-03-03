package com.spring.project.service.impl;

import com.spring.project.Exception.CustomExpiredJwtException;
import com.spring.project.dto.*;
import com.spring.project.email.EmailSender;
import com.spring.project.model.*;
import com.spring.project.repository.ClientRepository;
import com.spring.project.repository.PasswordResetTokenRepository;
import com.spring.project.repository.RoleRepsitory;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

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
            List<Client> clients = clientRepository.findAll();
              return clients.stream()
                    .map(client -> UserDataResponse.builder()
                                .id(client.getId())
                                .firstName(client.getFirstName())
                                .lastName(client.getLastName())
                                .email(client.getEmail())
                                .role(client.getRole())
                                .build())
                                .collect(Collectors.toList());
    }

    @Override
    public UserDataResponse getUserData(Integer id) {
        Client client = clientService.findClientById(id);
        return UserDataResponse.builder()
                .id(client.getId())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .email(client.getEmail())
                .role(client.getRole())
                .build();
    }

    @Override
    public void deleteUser(Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            Client client = clientService.findClientById(id);
            if(client.getRole().getName().equals("TRAINER")){
                List<TrainingClass> trainingClasses = trainingClassService.getTrainingClassesForTrainer(id);
                if(trainingClasses != null){
                    for(TrainingClass trainingClass : trainingClasses){
                            deleteTrainingClass(trainingClass.getId());
                    }
                }
            }
            confirmationTokenService.deleteByclient_Id(id);
            passwordResetTokenService.deleteByclient_Id(id);
            reservationService.deleteReservationByUserEmail(client.getEmail());
            clientRepository.deleteById(id);
        }
    }

    @Override
    public void updateUserRole(Integer id, RoleRequest roleRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            Role role = roleRepsitory.findById(roleRequest.getId()).orElse(null);
            Client client = clientService.findClientById(id);
            if(role.getName().equals("USER") && !clientRepository.findById(id).get().getRole().getName().equals("USER")){
                reservationService.deleteReservationByUserEmail(client.getEmail());
            }else if(role.getName().equals("TRAINER") && !clientRepository.findById(id).get().getRole().getName().equals("TRAINER")){
                List<TrainingClass> trainingClasses = trainingClassService.getTrainingClassesForTrainer(id);
                if(trainingClasses != null){
                    for(TrainingClass trainingClass : trainingClasses){
                        deleteTrainingClass(trainingClass.getId());
                    }
                }
            }
            client.setRole(role);
            clientRepository.save(client);
        }
    }

    @Override
    public List<TrainerResponse> getAllTrainers() {
        List<Client> trainers = clientRepository.getAllTrainers();
        return trainers.stream()
                .map(trainer -> TrainerResponse.builder()
                        .id(trainer.getId())
                                .firstName(trainer.getFirstName())
                                .lastName(trainer.getLastName()).build()
                        )
                .collect(Collectors.toList());
    }

    @Override
    public List<ReservationResponse> getAllReservations() {
            List<CourtReservation> reservations = reservationService.getAllReservations();
            return reservations.stream()
                    .map(reservation -> ReservationResponse.builder()
                            .localDate(reservation.getLocalDate().toString())
                            .hourSchedule(reservation.getHourSchedule())
                            .clientEmail(reservation.getEmail())
                            .court(reservation.getCourt()).build())
                    .collect(Collectors.toList());

    }

    @Override
    public List<SubscriptionResponse> getAllSubscriptions() {
        List<Subscription> subscriptions = subscriptionService.getAllSubscriptionPlans();
        return subscriptions.stream()
                .map(subscription -> SubscriptionResponse.builder()
                        .id(subscription.getId())
                        .subscriptionName(subscription.getSubscriptionName())
                        .subscriptionPrice(subscription.getSubscriptionPrice())
                        .subscriptionTime(subscription.getSubscriptionTime())
                        .subscriptionDescription(subscription.getSubscriptionDescription())
                        .build())
                .collect(Collectors.toList());
    }

    public void createSubscription(CreateSubscriptionRequest createSubscriptionRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            Subscription foundsubscription = subscriptionService.findBySubscriptionName(createSubscriptionRequest.getSubscriptionName());
            if (foundsubscription == null) {
                Subscription subscription = new Subscription(
                        createSubscriptionRequest.getSubscriptionName(),
                        createSubscriptionRequest.getSubscriptionPrice(),
                        createSubscriptionRequest.getSubscriptionTime(),
                        createSubscriptionRequest.getSubscriptionDescription()
                );
                subscriptionService.saveSubscription(subscription);
            }else{
                throw new EntityExistsException("There is already a subscription with this name");
            }
        }
    }

    @Override
    public void updateSubscription(Integer id, CreateSubscriptionRequest subscriptionRequest) {
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
    public SubscriptionResponse getSubscriptionById(Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            Subscription subscription = subscriptionService.findById(id).orElse(null);
            if(subscription != null){
                return SubscriptionResponse.builder()
                        .id(subscription.getId())
                        .subscriptionName(subscription.getSubscriptionName())
                        .subscriptionPrice(subscription.getSubscriptionPrice())
                        .subscriptionTime(subscription.getSubscriptionTime())
                        .subscriptionDescription(subscription.getSubscriptionDescription())
                        .build();
                }
            }
        throw new CustomExpiredJwtException("Session expired");
    }

    @Override
    public void deleteSubscription(Integer id) {
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
                    .map(trainingClass -> TrainingClassResponse.builder()
                            .id(trainingClass.getId())
                            .className(trainingClass.getClassName())
                            .duration(trainingClass.getDuration())
                            .startTime(trainingClass.getStartTime())
                            .intensity(trainingClass.getIntensity())
                            .localDate(trainingClass.getLocalDate())
                            .trainerId(trainingClass.getTrainer().getId())
                            .lastName(trainingClass.getTrainer().getLastName())
                            .firstName(trainingClass.getTrainer().getFirstName())
                            .build())
                            .collect(Collectors.toList());

        }else {
            throw new CustomExpiredJwtException("Session expired");
        }
    }

    @Override
    public TrainingClassResponse getTrainingClass(Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            TrainingClass trainingClass = trainingClassService.findById(id);
            return TrainingClassResponse.builder()
                    .id(trainingClass.getId())
                    .className(trainingClass.getClassName())
                    .duration(trainingClass.getDuration())
                    .startTime(trainingClass.getStartTime())
                    .intensity(trainingClass.getIntensity())
                    .localDate(trainingClass.getLocalDate())
                    .trainerId(trainingClass.getTrainer().getId())
                    .lastName(trainingClass.getTrainer().getLastName())
                    .firstName(trainingClass.getTrainer().getFirstName())
                    .build();
        }else {
            throw new CustomExpiredJwtException("Session expired");
        }
    }

    @Override
    public void createTrainingClass(TrainingClassRequest classRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            if (trainingClassService.getTrainingClassByName(classRequest.getClassName()) == null && clientService.findClientById(classRequest.getTrainerId()) != null) {
                   Client trainer = clientService.findClientById(classRequest.getTrainerId());
                    TrainingClass trainingClass = new TrainingClass(
                            classRequest.getClassName(),
                            classRequest.getDuration(),
                            classRequest.getStartTime(),
                            classRequest.getIntensity(),
                            classRequest.getLocalDate(),

                            trainer
                    );
                    trainingClassService.createTrainingClass(trainingClass);
                    String emailTemplate = loadEmailTemplateFromResource("trainingClassCreated.html");
                    emailTemplate = emailTemplate.replace("${email}", authentication.getName());
                    emailTemplate = emailTemplate.replace("${trainingClass}", classRequest.getClassName());
                    emailSender.send(authentication.getName(), emailTemplate, "Training class was created");
                }
            else {
                throw new EntityExistsException("There is already a class with this name");
            }
        }
    }

    @Override
    public void updateTrainingClass(Integer id, TrainingClassRequest trainingClassRequest) {
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
                    trainingClass.setTrainer(clientService.findClientById(trainingClassRequest.getTrainerId()));
                    trainingClassService.createTrainingClass(trainingClass);
                }else {
                    throw new EntityExistsException("There is a trainingClas with this name");
                }
            }else{
                throw new EntityNotFoundException("Training class could not be updated");
            }
        }
    }

    @Override
    public void deleteTrainingClass(Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            enrollmentTrainingClassService.deleteAllEnrollsForTrainingClass(id);
            trainingClassService.deleteTrainingClass(id);
         }
    }
}