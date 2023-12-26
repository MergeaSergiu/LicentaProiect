package com.spring.project.service;

import com.spring.project.Exception.ClientNotFoundException;
import com.spring.project.Exception.InvalidCredentialsException;
import com.spring.project.model.Client;
import com.spring.project.model.EnrollmentTrainingClass;
import com.spring.project.model.TrainingClass;
import com.spring.project.repository.ClientRepository;
import com.spring.project.token.ConfirmationToken;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ClientService implements UserDetailsService {

    private final static String CLIENT_NOT_FOUND_ERROR =
            "Client with email %s not found";
    private final ClientRepository clientRepository;

    private final ConfirmationTokenService confirmationTokenService;

    private final TrainingClassService trainingClassService;

    private final EnrollmentTrainingClassService enrollmentTrainingClassService;

    private final EmailService emailService;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return clientRepository.findByEmail(email).orElseThrow(() -> new ClientNotFoundException(String.format(CLIENT_NOT_FOUND_ERROR, email)));
    }

    public Client findClientByEmail(String email) {
        return clientRepository.findByEmail(email).orElse(null);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public String signUpClient(Client client) {
        boolean userExists = clientRepository.findByEmail(client.getEmail())
                .isPresent();
        if (userExists) {
            if (clientRepository.findByEmail(client.getEmail()).orElse(null).getEnabled()) {
                throw new InvalidCredentialsException("Account already exist");
            } else {
                Client alreadyExistClient = clientRepository.findByEmail(client.getEmail()).orElseThrow(() -> new ClientNotFoundException("Client does not exist"));
                String encodedPassword = passwordEncoder().encode(client.getPassword());

                alreadyExistClient.setFirstName(client.getFirstName());
                alreadyExistClient.setLastName(client.getLastName());
                alreadyExistClient.setPassword(encodedPassword);
                clientRepository.save(alreadyExistClient);

                String token = UUID.randomUUID().toString();
                ConfirmationToken foundConfirmationToken = confirmationTokenService.findTokenByUserId(Math.toIntExact(alreadyExistClient.getId()));
                foundConfirmationToken.setToken(token);
                foundConfirmationToken.setConfirmedAt(null);
                foundConfirmationToken.setCreatedAt(LocalDateTime.now());
                foundConfirmationToken.setExpiredAt(LocalDateTime.now().plusMinutes(60));
                foundConfirmationToken.setClient(alreadyExistClient);
                confirmationTokenService.saveConfirmationToken(foundConfirmationToken);
                return token;
            }
        } else {
            String token = UUID.randomUUID().toString();
            ConfirmationToken confirmationToken = new ConfirmationToken(
                    token,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(60),
                    client
            );
            String encodedPassword = passwordEncoder()
                    .encode(client.getPassword());
            client.setPassword(encodedPassword);
            clientRepository.save(client);
            confirmationTokenService.saveConfirmationToken(confirmationToken);
            return token;
        }
    }

    public List<Client> getAllClients() {
        return clientRepository.findAllByLastName();
    }

    public int enableClient(String email) {
        return clientRepository.enableClient(email);
    }

    public void resetClientPassword(Client client, String newPassword) {
        client.setPassword(passwordEncoder().encode(newPassword));
        clientRepository.save(client);
    }

    public List<TrainingClass> getTrainingClasses() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<TrainingClass> trainingClasses = new ArrayList<>();
        if(authentication.isAuthenticated()){
            trainingClasses = trainingClassService.getTrainingClasses();
        }
        return trainingClasses;

    }

    public void enrollUserToTrainingClass(String className) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            TrainingClass trainingClass = trainingClassService.getTrainingClassByName(className);
            if(trainingClass != null){
                Client client = clientRepository.findByEmail(authentication.getName()).orElse(null);
                if(client != null){
                    EnrollmentTrainingClass  enrollmentTrainingClass= new EnrollmentTrainingClass(
                            client,
                            trainingClass
                    );
                    enrollmentTrainingClassService.saveEnrollmentAction(enrollmentTrainingClass);
                    emailService.sendEnrollClassResponse(authentication.getName(),buildEnrollClassEmail(authentication.getName(),className));
                }
            }
        }
    }

    public List<String> getEnrollClasses() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            Client client = findClientByEmail(authentication.getName());
            List<EnrollmentTrainingClass> enrollClasses = enrollmentTrainingClassService.getClassesByUserId(client.getId());
            if (enrollClasses != null) {
                List<String> classesName = new ArrayList<>();
                for (EnrollmentTrainingClass enrollClass : enrollClasses) {
                    Integer training_class_id = enrollClass.getTrainingClass().getId();
                    TrainingClass trainingClass = trainingClassService.findById(training_class_id);
                    classesName.add(trainingClass.getClassName());
                }
                return classesName;
            }
        }
        return null;
    }



    private String buildEnrollClassEmail(String email, String className) {
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
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Thank for your enroll.</span>\n" +
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
                "            Thank you for the enrollment to the class : <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">" + className + "." + "</p>  </p></blockquote>\n <p>See you soon</p>" +
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
}
