package com.spring.project.mapper;

import com.spring.project.dto.TrainerCollaborationResponse;
import com.spring.project.model.TrainerCollaboration;
import org.springframework.stereotype.Component;

@Component
public class TrainerCollaborationMapper {

    public TrainerCollaborationResponse convertToDto(TrainerCollaboration trainerCollaboration){
        return TrainerCollaborationResponse.builder()
                .collaborationId(trainerCollaboration.getId())
                .firstNameUser(trainerCollaboration.getUser().getFirstName())
                .lastNameUser(trainerCollaboration.getUser().getLastName())
                .collaborationStatus(trainerCollaboration.getCollaborationStatus())
                .build();
    }
}
