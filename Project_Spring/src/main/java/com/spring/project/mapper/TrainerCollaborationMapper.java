package com.spring.project.mapper;

import com.spring.project.dto.TrainerCollaborationResponse;
import com.spring.project.model.CollaborationStatus;
import com.spring.project.model.TrainerCollaboration;
import com.spring.project.model.User;
import org.springframework.stereotype.Component;

@Component
public class TrainerCollaborationMapper {

    public TrainerCollaborationResponse convertToDtoForTrainer(TrainerCollaboration trainerCollaboration){
        return TrainerCollaborationResponse.builder()
                .collaborationId(trainerCollaboration.getId())
                .firstName(trainerCollaboration.getUser().getFirstName())
                .lastName(trainerCollaboration.getUser().getLastName())
                .email(trainerCollaboration.getUser().getEmail())
                .startTime(trainerCollaboration.getStartDate())
                .endTime(trainerCollaboration.getEndTime())
                .collaborationStatus(trainerCollaboration.getCollaborationStatus())
                .build();
    }

    public TrainerCollaboration createFromDto(User user, User trainer){
        return TrainerCollaboration.builder()
                .startDate(null)
                .endTime(null)
                .collaborationStatus(CollaborationStatus.PENDING)
                .trainer(trainer)
                .user(user)
                .build();
    }

    public TrainerCollaborationResponse convertToDtoForUser(TrainerCollaboration trainerCollaboration){
        return TrainerCollaborationResponse.builder()
                .collaborationId(trainerCollaboration.getId())
                .firstName(trainerCollaboration.getTrainer().getFirstName())
                .lastName(trainerCollaboration.getTrainer().getLastName())
                .email(trainerCollaboration.getTrainer().getEmail())
                .startTime(trainerCollaboration.getStartDate())
                .endTime(trainerCollaboration.getEndTime())
                .collaborationStatus(trainerCollaboration.getCollaborationStatus())
                .build();
    }
}
