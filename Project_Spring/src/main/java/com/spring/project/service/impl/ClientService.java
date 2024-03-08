package com.spring.project.service.impl;

import com.spring.project.Exception.ClientNotFoundException;
import com.spring.project.Exception.CustomExpiredJwtException;
import com.spring.project.dto.AuthenticationResponse;
import com.spring.project.dto.StatusEnrollResponse;
import com.spring.project.dto.TrainingClassResponse;
import com.spring.project.mapper.TrainingClassMapper;
import com.spring.project.model.Client;
import com.spring.project.model.EnrollmentTrainingClass;
import com.spring.project.model.TrainingClass;
import com.spring.project.repository.ClientRepository;
import com.spring.project.service.ConfirmationTokenService;
import com.spring.project.service.EnrollmentTrainingClassService;
import com.spring.project.service.TrainingClassService;
import com.spring.project.token.ConfirmationToken;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ClientService implements UserDetailsService {

    private final static String CLIENT_NOT_FOUND_ERROR = "Client with email %s not found";
    private final ClientRepository clientRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final TrainingClassService trainingClassService;
    private final EnrollmentTrainingClassService enrollmentTrainingClassService;
    private final EmailServiceImpl emailService;
    private final TrainingClassMapper trainingClassMapper;


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

    private String loadEmailTemplateFromResource(String fileName) {
        try {
            Resource resource = new ClassPathResource(fileName);
            return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public Client findClientById(Integer id){
        return clientRepository.findById(id).orElse(null);
    }

    public String signUpClient(Client client) {
        boolean userExists = clientRepository.findByEmail(client.getEmail())
                .isPresent();
        if (userExists) {
            if (clientRepository.findByEmail(client.getEmail()).orElse(null).getEnabled()) {
                throw new EntityExistsException("Account already exist");
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

    public int enableClient(String email) {
        return clientRepository.enableClient(email);
    }

    public void resetClientPassword(Client client, String newPassword) {
        client.setPassword(passwordEncoder().encode(newPassword));
        clientRepository.save(client);
    }

    public List<TrainingClassResponse> getTrainingClasses() {
            List<TrainingClass> trainingClasses = trainingClassService.getTrainingClasses();
            return trainingClasses.stream()
                    .map(trainingClass -> trainingClassMapper.convertToDto(trainingClass)).collect(Collectors.toList());
    }


    public void enrollUserToTrainingClass(Integer id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()) {
            TrainingClass trainingClass = trainingClassService.findById(id);
            if (trainingClass != null) {
                Client client = clientRepository.findByEmail(authentication.getName()).orElse(null);
                if (client != null) {
                    EnrollmentTrainingClass enrollmentTrainingClass = new EnrollmentTrainingClass(
                            client,
                            trainingClass
                    );
                    enrollmentTrainingClassService.saveEnrollmentAction(enrollmentTrainingClass);
                    String emailTemplate = loadEmailTemplateFromResource("enrollClassEmail.html");
                    emailTemplate = emailTemplate.replace("${email}", authentication.getName());
                    emailTemplate = emailTemplate.replace("{enrollClass}", trainingClass.getClassName());
                    emailService.send(authentication.getName(), emailTemplate, "Thanks for joining the class");
                }
            }
        }else {
            throw new CustomExpiredJwtException("Session expired");
        }
    }

    public StatusEnrollResponse checkEnrollmentStatus(Integer trainingClassId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
                Client client = clientRepository.findByEmail(authentication.getName()).orElse(null);
                if(client != null) {
                    List<EnrollmentTrainingClass> listEnrollments = enrollmentTrainingClassService.getClassesByUserId(client.getId());
                    if (listEnrollments != null) {
                        for (EnrollmentTrainingClass enrollment : listEnrollments) {
                            if (enrollment.getTrainingClass().getId() == trainingClassId) {
                                return StatusEnrollResponse.builder()
                                        .status("enrolled")
                                        .build();
                            }
                        }
                    }
                    return StatusEnrollResponse.builder()
                            .status("notEnrolled")
                            .build();
                }
        }
        throw new CustomExpiredJwtException("Session expired");
    }

    public void unEnrollUserFromTrainingClass(Integer classId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            TrainingClass trainingClass = trainingClassService.findById(classId);
            if (trainingClass != null) {
                enrollmentTrainingClassService.deleteEnrollmentForUser(trainingClass.getId(), clientRepository.findByEmail(authentication.getName()).get().getId());
            } else {
                throw new EntityNotFoundException("The training Class does not exist");
            }
        }
    }

}
