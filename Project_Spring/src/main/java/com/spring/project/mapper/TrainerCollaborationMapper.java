package com.spring.project.mapper;

import com.spring.project.dto.TrainerCollaborationResponse;
import com.spring.project.model.TrainerCollaboration;
import org.springframework.stereotype.Component;

@Component
public class TrainerCollaborationMapper {

    public TrainerCollaborationResponse convertToDtoForTrainer(TrainerCollaboration trainerCollaboration){
        return TrainerCollaborationResponse.builder()
                .collaborationId(trainerCollaboration.getId())
                .firstName(trainerCollaboration.getUser().getFirstName())
                .lastName(trainerCollaboration.getUser().getLastName())
                .collaborationStatus(trainerCollaboration.getCollaborationStatus())
                .build();
    }

    public TrainerCollaborationResponse convertToDtoForUser(TrainerCollaboration trainerCollaboration){
        return TrainerCollaborationResponse.builder()
                .collaborationId(trainerCollaboration.getId())
                .firstName(trainerCollaboration.getTrainer().getFirstName())
                .lastName(trainerCollaboration.getTrainer().getLastName())
                .collaborationStatus(trainerCollaboration.getCollaborationStatus())
                .build();
    }
}
