package com.spring.project.service.impl;

import com.spring.project.Exception.CustomExpiredJwtException;
import com.spring.project.Exception.EmailNotAvailableException;
import com.spring.project.dto.*;
import com.spring.project.email.EmailSender;
import com.spring.project.model.*;
import com.spring.project.repository.ClientRepository;
import com.spring.project.service.*;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

    private EmailValidator emailValidator;
    private PasswordValidator passwordValidator;
    private final ClientService clientService;
    private final EmailSender emailSender;
    private final ReservationService reservationService;
    private final SubscriptionService subscriptionService;
    private final TrainingClassService trainingClassService;
    private final ClientRepository clientRepository;
    private final EnrollmentTrainingClassService enrollmentTrainingClassService;

    private String loadEmailTemplateFromResource(String fileName) {
        try {
            Resource resource = new ClassPathResource(fileName);
            return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
    public TrainerResponse createTrainer(TrainerRequest request) {
                boolean isValidEmail = emailValidator.test(request.getEmail());
                if (!isValidEmail) {
                    throw new EmailNotAvailableException("Email is not valid");
                }
                boolean isValidPassword = passwordValidator.test(request.getPassword());
                if (!isValidPassword) {
                    throw new EmailNotAvailableException("Password do not respect the criteria");
                }
                Client trainer = new Client(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPassword(),
                        ClientRole.TRAINER
                );
                String receivedToken = clientService.signUpClient(trainer);
                String link = "http://localhost:8080/project/auth/confirm?token=" + receivedToken;
                String emailTemplate = loadEmailTemplateFromResource("confirmAccountEmail.hmtl");
                emailTemplate = emailTemplate.replace("${link}", link);
                emailTemplate = emailTemplate.replace("${resetEmail}", request.getEmail());
                emailSender.send(request.getEmail(), emailTemplate, "Trainer account was created.Please activate your account.");
                return TrainerResponse.builder()
                         .id(10)
                         .lastName(request.getLastName())
                         .firstName(request.getFirstName())
                         .build();
    }

    public List<ClientResponse> getAllClients() {
            List<Client> clients = clientRepository.findAll();
              return clients.stream()
                    .map(client -> ClientResponse.builder()
                                .firstName(client.getFirstName())
                                .lastName(client.getLastName())
                                .email(client.getEmail()).build())
                    .collect(Collectors.toList());
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