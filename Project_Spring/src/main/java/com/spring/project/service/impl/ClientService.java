package com.spring.project.service.impl;

import com.spring.project.Exception.ClientNotFoundException;
import com.spring.project.Exception.CustomExpiredJwtException;
import com.spring.project.dto.StatusEnrollResponse;
import com.spring.project.dto.TrainingClassResponse;
import com.spring.project.mapper.ConfirmationTokenMapper;
import com.spring.project.mapper.EnrollmentClassMapper;
import com.spring.project.mapper.StatusEnrollResponseMapper;
import com.spring.project.mapper.TrainingClassMapper;
import com.spring.project.model.User;
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
import java.time.LocalDateTime;
import java.util.List;
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
    private final StatusEnrollResponseMapper statusEnrollResponseMapper;
    private final EnrollmentClassMapper enrollmentClassMapper;
    private final ConfirmationTokenMapper confirmationTokenMapper;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return clientRepository.findByEmail(email).orElseThrow(() -> new ClientNotFoundException(String.format(CLIENT_NOT_FOUND_ERROR, email)));
    }

    public User findClientByEmail(String email) {
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

    public User findClientById(Long id){
        return clientRepository.findById(id).orElse(null);
    }

    public String signUpClient(User user) {
        boolean userExists = clientRepository.findByEmail(user.getEmail()).isPresent();
        if (userExists) {
            if (clientRepository.findByEmail(user.getEmail()).orElse(null).getEnabled()) {
                throw new EntityExistsException("Account already exist");
            } else {
                User alreadyExistUser = clientRepository.findByEmail(user.getEmail()).orElseThrow(() -> new ClientNotFoundException("Client does not exist"));
                String encodedPassword = passwordEncoder().encode(user.getPassword());

                alreadyExistUser.setFirstName(user.getFirstName());
                alreadyExistUser.setLastName(user.getLastName());
                alreadyExistUser.setPassword(encodedPassword);
                clientRepository.save(alreadyExistUser);

                String token = UUID.randomUUID().toString();
                ConfirmationToken foundConfirmationToken = confirmationTokenService.findTokenByUserId(alreadyExistUser.getId());
                foundConfirmationToken.setToken(token);
                foundConfirmationToken.setConfirmedAt(null);
                foundConfirmationToken.setCreatedAt(LocalDateTime.now());
                foundConfirmationToken.setExpiredAt(LocalDateTime.now().plusMinutes(60));
                foundConfirmationToken.setUser(alreadyExistUser);
                confirmationTokenService.saveConfirmationToken(foundConfirmationToken);
                return token;
            }
        } else {
            String token = UUID.randomUUID().toString();
            ConfirmationToken confirmationToken = confirmationTokenMapper.createConfirmationToken(token,LocalDateTime.now(), user);
            String encodedPassword = passwordEncoder().encode(user.getPassword());
            user.setPassword(encodedPassword);
            clientRepository.save(user);
            confirmationTokenService.saveConfirmationToken(confirmationToken);
            return token;
        }
    }

    public int enableClient(String email) {
        return clientRepository.enableClient(email);
    }

    public void resetClientPassword(User user, String newPassword) {
        user.setPassword(passwordEncoder().encode(newPassword));
        clientRepository.save(user);
    }

    public void enrollUserToTrainingClass(Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()) {
            TrainingClass trainingClass = trainingClassService.findById(id);
            if (trainingClass != null) {
                User user = clientRepository.findByEmail(authentication.getName()).orElse(null);
                if (user != null) {
                    EnrollmentTrainingClass enrollmentTrainingClass = enrollmentClassMapper.createEnrollmentForUser(trainingClass,user);
                    enrollmentTrainingClassService.saveEnrollmentAction(enrollmentTrainingClass);
                    String emailTemplate = loadEmailTemplateFromResource("enrollClassEmail.html");
                    emailTemplate = emailTemplate.replace("${email}", authentication.getName());
                    emailTemplate = emailTemplate.replace("{enrollClass}", trainingClass.getClassName());
                    emailService.send(authentication.getName(), emailTemplate, "Thank you for joining the class");
                }
            }
        }else {
            throw new CustomExpiredJwtException("Session expired");
        }
    }

    public StatusEnrollResponse checkEnrollmentStatus(Long trainingClassId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
                User user = clientRepository.findByEmail(authentication.getName()).orElse(null);
                if(user != null) {
                    List<EnrollmentTrainingClass> listEnrollments = enrollmentTrainingClassService.getClassesByUserId(user.getId());
                    if (listEnrollments != null) {
                        for (EnrollmentTrainingClass enrollment : listEnrollments) {
                            if (enrollment.getTrainingClass().getId() == trainingClassId) {
                                return statusEnrollResponseMapper.createStatusEnrollResponse("enrolled");
                            }
                        }
                    }
                    return statusEnrollResponseMapper.createStatusEnrollResponse("notEnrolled");
                }
        }
        throw new CustomExpiredJwtException("Session expired");
    }

    public void unEnrollUserFromTrainingClass(Long classId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            User user = clientRepository.findByEmail(authentication.getName()).orElse(null);
                enrollmentTrainingClassService.deleteEnrollmentForUser(classId, user.getId());
            } else {
                throw new EntityNotFoundException("The training Class does not exist");
            }
        }

}
