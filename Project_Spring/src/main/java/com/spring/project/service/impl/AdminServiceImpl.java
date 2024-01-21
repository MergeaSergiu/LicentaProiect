package com.spring.project.service.impl;

import com.spring.project.Exception.EmailNotAvailableException;
import com.spring.project.Exception.InvalidCredentialsException;
import com.spring.project.dto.*;
import com.spring.project.email.EmailSender;
import com.spring.project.model.*;
import com.spring.project.repository.ClientRepository;
import com.spring.project.service.*;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
    private final SubscriptionService subscriptionService;
    private final TrainingClassService trainingClassService;
    private final ClientRepository clientRepository;
    private final EnrollmentTrainingClassService enrollmentTrainingClassService;

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
                emailSender.send(request.getEmail(), buildEmail(request.getFirstName(), link), "Trainer account was created");
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


    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }



    public void createSubscription(CreateSubscriptionRequest createSubscriptionRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            Subscription subscription = new Subscription(
                    createSubscriptionRequest.getSubscriptionName(),
                    createSubscriptionRequest.getSubscriptionPrice(),
                    createSubscriptionRequest.getSubscriptionTime(),
                    createSubscriptionRequest.getSubscriptionDescription()
            );
            subscriptionService.saveSubscription(subscription);
        }
    }

    public void updateSubscription(Integer id, Map<String, Object> fields) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getName() != null && authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            Subscription subscription = subscriptionService.findById(id).orElse(null);
            if(subscription != null) {
                fields.forEach((key, value) -> {
                    switch (key) {
                        case "subscriptionName" -> subscription.setSubscriptionName((String) value);
                        case "subscriptionPrice" -> subscription.setSubscriptionPrice((Double) value);
                        case "subscriptionTime" -> subscription.setSubscriptionTime((Integer) value);
                        case "subscriptionDescription" -> subscription.setSubscriptionDescription((String) value);
                        default -> throw new IllegalArgumentException("Invalid field:" + key);
                    }
                });
                subscriptionService.saveSubscription(subscription);
            }else {
                throw new EntityNotFoundException("There is no subscription with this name");
            }
        }
    }

    public void deleteSubscription(Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getName() != null && authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
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
                emailSender.send(authentication.getName(), buildCreateClassEmail(authentication.getName(), classRequest.getClassName()),"Training class was created");
            } else {
                throw new EntityExistsException("There is already a class with this name");
            }
        }
    }

    private String buildCreateClassEmail(String email, String className) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">The Training Class was created.</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + email + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">" +
                "            The Training class : <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">" + className + " was created." + "</p>  </p></blockquote>\n <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
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