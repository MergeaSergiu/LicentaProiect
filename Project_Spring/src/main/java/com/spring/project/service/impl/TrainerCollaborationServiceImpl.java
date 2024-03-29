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
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
            if(user != null){
                User trainer = clientRepository.findById(trainerId).orElse(null);
                if(trainer != null){
                    TrainerCollaboration trainerCollaboration = TrainerCollaboration.builder()
                            .startDate(LocalDate.now())
                            .collaborationStatus(CollaborationStatus.PENDING)
                            .trainer(trainer)
                            .user(user)
                            .build();
                    trainerCollaborationRepository.save(trainerCollaboration);
                    String emailTemplateTrainer = loadEmailTemplateFromResource("collabRequestReceive.html");
                    emailTemplateTrainer = emailTemplateTrainer.replace("${email}", trainer.getEmail());
                    emailTemplateTrainer = emailTemplateTrainer.replace("${userName}", user.getFirstName()+ " " + user.getLastName());
                    emailSender.send(trainer.getEmail(), emailTemplateTrainer, "Collaboration Request");
                    String emailTemplateUser = loadEmailTemplateFromResource("collabRequestSent.html");
                    emailTemplateUser = emailTemplateUser.replace("${email}", authentication.getName());
                    emailTemplateUser = emailTemplateUser.replace("${trainerName}", trainer.getFirstName() + " " + trainer.getLastName());
                    emailSender.send(authentication.getName(), emailTemplateUser, "Collaboration Request");
                }else{
                    throw new ClientNotFoundException("Trainer does not exist");
                }
            }else{
                throw new ClientNotFoundException("User does not exist");
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
                            .map(collaboration -> trainerCollaborationMapper.convertToDto(collaboration)).collect(Collectors.toList());
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
