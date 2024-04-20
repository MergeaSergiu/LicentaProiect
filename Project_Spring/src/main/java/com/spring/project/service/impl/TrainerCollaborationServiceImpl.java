package com.spring.project.service.impl;

import com.spring.project.dto.TrainerCollaborationResponse;
import com.spring.project.email.EmailSender;
import com.spring.project.mapper.TrainerCollaborationMapper;
import com.spring.project.model.CollaborationStatus;
import com.spring.project.model.TrainerCollaboration;
import com.spring.project.model.User;
import com.spring.project.repository.ClientRepository;
import com.spring.project.repository.TrainerCollaborationRepository;
import com.spring.project.service.TrainerCollaborationService;
import com.spring.project.util.UtilMethods;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
    private final UtilMethods utilMethods;


    @Override
    public void sendCollaborationRequest(Long trainerId, String authorization) {
            String username = utilMethods.extractUsernameFromAuthorizationHeader(authorization);
            User user = clientRepository.findByEmail(username).orElse(null);
            if(user == null){
                throw new EntityNotFoundException("User does not exist");
            }
            User trainer = clientRepository.findById(trainerId).orElse(null);
            if(trainer == null){
                throw new EntityNotFoundException("Trainer does not exist");
            }
                List<CollaborationStatus> activeStatuses = Arrays.asList(CollaborationStatus.ACCEPTED, CollaborationStatus.PENDING);
                List<TrainerCollaboration> existingCollaboration = trainerCollaborationRepository.findByUserAndCollaborationStatusIn(user, activeStatuses);
                if(existingCollaboration.size() == 0) {
                    TrainerCollaboration trainerCollaboration = trainerCollaborationMapper.createFromDto(user,trainer);
                    trainerCollaborationRepository.save(trainerCollaboration);
                    String emailTemplateTrainer = utilMethods.loadEmailTemplateFromResource("collabRequestReceive.html");
                    emailTemplateTrainer = emailTemplateTrainer.replace("${email}", trainer.getEmail());
                    emailTemplateTrainer = emailTemplateTrainer.replace("${userName}", user.getFirstName() + " " + user.getLastName());
                    emailSender.send(trainer.getEmail(), emailTemplateTrainer, "Collaboration Request");
                    String emailTemplateUser = utilMethods.loadEmailTemplateFromResource("collabRequestSent.html");
                    emailTemplateUser = emailTemplateUser.replace("${user}", user.getFirstName()+" " + user.getLastName());
                    emailTemplateUser = emailTemplateUser.replace("${trainerName}", trainer.getFirstName() + " " + trainer.getLastName());
                    emailSender.send(username, emailTemplateUser, "Collaboration Request");
                }else{
                    throw new EntityExistsException("There is already an active reservation");
                }

    }

    @Override
    public List<TrainerCollaborationResponse> getCollaborationForTrainer(String authorization) {
            String username = utilMethods.extractUsernameFromAuthorizationHeader(authorization);
            User currentTrainer = clientRepository.findByEmail(username).orElse(null);
            if(currentTrainer == null){
                throw new EntityNotFoundException("Trainer does not exist");
            }
            List<TrainerCollaboration> collaborationListForTrainer = trainerCollaborationRepository.findAllByTrainer_Id(currentTrainer.getId());
            if(collaborationListForTrainer.size() > 0) {
                return collaborationListForTrainer.stream()
                        .map(trainerCollaborationMapper::convertToDtoForTrainer).collect(Collectors.toList());
            }
            else{
                return new ArrayList<>();
            }
    }

    @Override
    public void acceptUserCollaboration(Long collaborationId) {
            TrainerCollaboration trainerCollaboration = trainerCollaborationRepository.findById(collaborationId).orElse(null);
            if(trainerCollaboration == null){
                throw new EntityNotFoundException("Trainer Collaboration does not exist");
            }
                trainerCollaboration.setCollaborationStatus(CollaborationStatus.ACCEPTED);
                trainerCollaboration.setStartDate(LocalDate.now());
                trainerCollaborationRepository.save(trainerCollaboration);
                User user = trainerCollaboration.getUser();
                User trainer = trainerCollaboration.getTrainer();
                String emailTemplateTrainer = utilMethods.loadEmailTemplateFromResource("collabAccepted.html");
                emailTemplateTrainer = emailTemplateTrainer.replace("${userName}", user.getFirstName() + " " + user.getLastName());
                emailTemplateTrainer = emailTemplateTrainer.replace("${trainerName}", trainer.getFirstName() + " " + trainer.getLastName());
                emailTemplateTrainer = emailTemplateTrainer.replace("${startDate}", LocalDate.now().toString());
                emailSender.send(user.getEmail(), emailTemplateTrainer, "Collaboration Accepted");
    }

    @Override
    public void declineUserCollaboration(Long collaborationId, String authorization) {
            String username = utilMethods.extractUsernameFromAuthorizationHeader(authorization);
            User user = clientRepository.findByEmail(username).orElse(null);
            if(user == null) {
                throw new EntityNotFoundException("User does not exist");
            }
            TrainerCollaboration trainerCollaboration = trainerCollaborationRepository.findById(collaborationId).orElse(null);
            if(trainerCollaboration == null){
                throw new EntityNotFoundException("Collaboration does not exist");
            }
            User trainer = trainerCollaboration.getTrainer();
            if(trainer == null){
                throw new EntityNotFoundException("Trainer does not exist");
            }
                trainerCollaborationRepository.deleteById(collaborationId);
                String emailTemplateTrainer = utilMethods.loadEmailTemplateFromResource("declineEmail.html");
                emailTemplateTrainer = emailTemplateTrainer.replace("${userName}", user.getFirstName() + " " + user.getLastName());
                emailTemplateTrainer = emailTemplateTrainer.replace("${trainerName}", trainer.getFirstName() + " " + trainer.getLastName());
                emailSender.send(user.getEmail(), emailTemplateTrainer, "Collaboration Declined");
    }

    @Override
    public void finishCollaboration(Long collaborationId) {
            TrainerCollaboration trainerCollaboration = trainerCollaborationRepository.findById(collaborationId).orElse(null);
            if(trainerCollaboration == null){
                throw new EntityNotFoundException("This collaboration does not exist");
            }
            trainerCollaborationRepository.updateCollaborationStatus(trainerCollaboration.getId(), LocalDate.now(), CollaborationStatus.ENDED);
    }

    @Override
    public List<TrainerCollaborationResponse> getCollaborationForUser(String authorization) {
            String username = utilMethods.extractUsernameFromAuthorizationHeader(authorization);
            User currentUser = clientRepository.findByEmail(username).orElse(null);
            if(currentUser == null){
                throw new EntityNotFoundException("User does not exist");
            }
            List<TrainerCollaboration> collaborationListForTrainer = trainerCollaborationRepository.findAllByUser_Id(currentUser.getId());
            if(collaborationListForTrainer.size() > 0) {
                return collaborationListForTrainer.stream()
                        .map(trainerCollaborationMapper::convertToDtoForUser).collect(Collectors.toList());
            }
            else{
                return new ArrayList<>();
            }
    }
}
