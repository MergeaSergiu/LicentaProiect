package com.spring.project.dto;

import com.spring.project.model.CollaborationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TrainerCollaborationResponse {

    private Long collaborationId;
    private String firstNameUser;
    private String lastNameUser;
    private CollaborationStatus collaborationStatus;
}
