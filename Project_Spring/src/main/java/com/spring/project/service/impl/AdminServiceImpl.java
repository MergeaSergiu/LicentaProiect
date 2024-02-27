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
import java.time.LocalDate;
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
                         .email(request.getEmail())
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
                if (subscriptionRequest.getSubscriptionName().equals(subscriptionService.findById(id).get().getSubscriptionName()) || (subscriptionService.findBySubscriptionName(subscriptionRequest.getSubscriptionName()) == null)) {
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

    public void deleteSubscription(Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            subscriptionService.deleteSubscription(id);
        }
    }

    public void createTrainingClass(CreateClassRequest classRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getName() != null && authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            if (trainingClassService.getTrainingClassByName(classRequest.getClassName()) == null) {
                Client client = clientService.findClientByEmail(classRequest.getTrainerEmail());
                TrainingClass trainingClass = new TrainingClass(
                        classRequest.getClassName(),
                        classRequest.getDuration(),
                        classRequest.getIntensity(),
                        classRequest.getLocalDate(),
                        client
                );
                trainingClassService.createTrainingClass(trainingClass);
                String emailTemplate = loadEmailTemplateFromResource("trainingClassCreated.html");
                emailTemplate = emailTemplate.replace("${email}", authentication.getName());
                emailTemplate = emailTemplate.replace("${trainingClass}", classRequest.getClassName());
                emailSender.send(authentication.getName(), emailTemplate,"Training class was created");
            } else {
                throw new EntityExistsException("There is already a class with this name");
            }
        }
    }

    public void updateTrainingClass(Integer id, Map<String, Object> fields) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getName() != null && authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            TrainingClass trainingClass = trainingClassService.findById(id);

            fields.forEach((key, value) -> {
                switch (key) {
                    case "className" -> {
                        TrainingClass foundTrainingClass = trainingClassService.getTrainingClassByName((String) value);
                        if(foundTrainingClass == null){
                            trainingClass.setClassName((String) value);
                        }
                    }
                    case "duration" -> trainingClass.setDuration((Integer) value);
                    case "intensity" -> trainingClass.setIntensity((String) value);
                    case "localDate" -> {
                        LocalDate localDate = LocalDate.parse((String) value);
                        trainingClass.setLocalDate(localDate);
                    }
                    case "trainerEmail" -> {
                        if (clientService.findClientByEmail((String) value) != null && clientService.findClientByEmail((String) value).getClientRole().toString().equals("TRAINER")) {
                            Client trainer = clientService.findClientByEmail((String) value);
                            trainingClass.setTrainer(trainer);
                        }
                    }
                    default -> throw new IllegalArgumentException("Invalid field:" + key);
                }
            });
            trainingClassService.createTrainingClass(trainingClass);
        }
    }

    @Override
    public void deleteTrainingClass(String className) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getName() != null && authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            Integer trainingClassId = trainingClassService.getTrainingClassByName(className).getId();
            enrollmentTrainingClassService.deleteAllEnrollsForTrainingClass(trainingClassId);
            trainingClassService.deleteTrainingClass(className);
        }
    }
}