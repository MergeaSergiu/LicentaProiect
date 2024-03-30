package com.spring.project.service.impl;

import com.spring.project.Exception.ClientNotFoundException;
import com.spring.project.Exception.CustomExpiredJwtException;
import com.spring.project.dto.TrainerCollaborationResponse;
import com.spring.project.email.EmailSender;
import com.spring.project.mapper.TrainerCollaborationMapper;
import com.spring.project.model.CollaborationStatus;
import com.spring.project.model.TrainerCollaboration;
import com.spring.project.model.User;
import com.spring.project.repository.ClientRepository;
import com.spring.project.repository.TrainerCollaborationRepository;
import com.spring.project.service.TrainerCollaborationService;
import jakarta.persistence.EntityExistsException;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TrainerCollaborationServiceImpl implements TrainerCollaborationService {

    private final TrainerCollaborationRepository trainerCollaborationRepository;

    private final ClientRepository clientRepository;

    private final EmailSender emailSender;
    private final TrainerCollaborationMapper trainerCollaborationMapper;

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
    public void sendCollaborationRequest(Long trainerId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            User user = clientRepository.findByEmail(authentication.getName()).orElse(null);
            User trainer = clientRepository.findById(trainerId).orElse(null);
            if(user != null && trainer != null){
                List<CollaborationStatus> activeStatuses = Arrays.asList(CollaborationStatus.ACCEPTED, CollaborationStatus.PENDING);
                List<TrainerCollaboration> existingCollaboration = trainerCollaborationRepository.findByUserAndCollaborationStatusIn(user, activeStatuses);
                if(existingCollaboration.size() == 0) {
                    TrainerCollaboration trainerCollaboration = TrainerCollaboration.builder()
                            .startDate(LocalDate.now())
                            .collaborationStatus(CollaborationStatus.PENDING)
                            .trainer(trainer)
                            .user(user)
                            .build();
                    trainerCollaborationRepository.save(trainerCollaboration);
                    String emailTemplateTrainer = loadEmailTemplateFromResource("collabRequestReceive.html");
                    emailTemplateTrainer = emailTemplateTrainer.replace("${email}", trainer.getEmail());
                    emailTemplateTrainer = emailTemplateTrainer.replace("${userName}", user.getFirstName() + " " + user.getLastName());
                    emailSender.send(trainer.getEmail(), emailTemplateTrainer, "Collaboration Request");
                    String emailTemplateUser = loadEmailTemplateFromResource("collabRequestSent.html");
                    emailTemplateUser = emailTemplateUser.replace("${email}", authentication.getName());
                    emailTemplateUser = emailTemplateUser.replace("${trainerName}", trainer.getFirstName() + " " + trainer.getLastName());
                    emailSender.send(authentication.getName(), emailTemplateUser, "Collaboration Request");
                }else{
                    throw new EntityExistsException("There is already an active reservation");
                }
                }else{
                    throw new ClientNotFoundException("Trainer/User does not exist");
                }
        }else{
            throw new CustomExpiredJwtException("Session has expired");
        }
    }

    @Override
    public List<TrainerCollaborationResponse> getCollaborationForTrainer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            User currentTrainer = clientRepository.findByEmail(authentication.getName()).orElse(null);
            if(currentTrainer != null){
                List<TrainerCollaboration> collaborationListForTrainer = trainerCollaborationRepository.findAllByTrainer_Id(currentTrainer.getId());
                if(collaborationListForTrainer.size() > 0) {
                    return collaborationListForTrainer.stream()
                            .map(collaboration -> trainerCollaborationMapper.convertToDtoForTrainer(collaboration)).collect(Collectors.toList());
                }
                else{
                    return new ArrayList<>();
                    }
                }else {
                    throw new ClientNotFoundException("Trainer does not exist");
                }
            }else{
            throw new CustomExpiredJwtException("Session has expired");
        }
    }

    @Override
    public void acceptUserCollaboration(Long collaborationId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            TrainerCollaboration trainerCollaboration = trainerCollaborationRepository.findById(collaborationId).orElse(null);
            if(trainerCollaboration != null){
                trainerCollaboration.setCollaborationStatus(CollaborationStatus.ACCEPTED);
                trainerCollaboration.setStartDate(LocalDate.now());
                trainerCollaborationRepository.save(trainerCollaboration);
                User user = trainerCollaboration.getUser();
                User trainer = trainerCollaboration.getTrainer();
                String emailTemplateTrainer = loadEmailTemplateFromResource("collabAccepted.html");
                emailTemplateTrainer = emailTemplateTrainer.replace("${userName}", user.getFirstName() + " " + user.getLastName());
                emailTemplateTrainer = emailTemplateTrainer.replace("${trainerName}", trainer.getFirstName() + " " + trainer.getLastName());
                emailTemplateTrainer = emailTemplateTrainer.replace("${startDate}", LocalDate.now().toString());
                emailSender.send(user.getEmail(), emailTemplateTrainer, "Collaboration Accepted");
            }
        }else{
            throw new CustomExpiredJwtException("Session has expired");
        }
    }

    @Override
    public void declineUserCollaboration(Long collaborationId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            User user = clientRepository.findByEmail(authentication.getName()).orElse(null);
            if(user != null) {
                TrainerCollaboration trainerCollaboration = trainerCollaborationRepository.findById(collaborationId).orElse(null);
                User trainer = trainerCollaboration.getTrainer();
                if(trainer != null) {
                    trainerCollaborationRepository.deleteById(collaborationId);
                    String emailTemplateTrainer = loadEmailTemplateFromResource("declineEmail.html");
                    emailTemplateTrainer = emailTemplateTrainer.replace("${userName}", user.getFirstName() + " " + user.getLastName());
                    emailTemplateTrainer = emailTemplateTrainer.replace("${trainerName}", trainer.getFirstName() + " " + trainer.getLastName());
                    emailSender.send(user.getEmail(), emailTemplateTrainer, "Collaboration Declined");
                }else{
                    throw new CustomExpiredJwtException("Trainer does not exist");
                    }
                }else{
                    throw new ClientNotFoundException("User does not exist");
                }
            }else{
                    throw new CustomExpiredJwtException("Session has expired");
            }
    }

    @Override
    public void finishCollaboration(Long collaborationId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            TrainerCollaboration trainerCollaboration = trainerCollaborationRepository.findById(collaborationId).orElse(null);
            if(trainerCollaboration != null){
                trainerCollaborationRepository.updateCollaborationStatus(trainerCollaboration.getId(), CollaborationStatus.ENDED);
            }else{
                throw new ClientNotFoundException("This collaboration does not exist");
            }
        }else{
            throw new CustomExpiredJwtException("Session has expired");
        }
    }

    @Override
    public List<TrainerCollaborationResponse> getCollaborationForUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            User currentUser = clientRepository.findByEmail(authentication.getName()).orElse(null);
            if(currentUser != null){
                List<TrainerCollaboration> collaborationListForTrainer = trainerCollaborationRepository.findAllByUser_Id(currentUser.getId());
                if(collaborationListForTrainer.size() > 0) {
                    return collaborationListForTrainer.stream()
                            .map(collaboration -> trainerCollaborationMapper.convertToDtoForUser(collaboration)).collect(Collectors.toList());
                }
                else{
                    return new ArrayList<>();
                }
            }else {
                throw new ClientNotFoundException("Trainer does not exist");
            }
        }else{
            throw new CustomExpiredJwtException("Session has expired");
        }
    }
}
