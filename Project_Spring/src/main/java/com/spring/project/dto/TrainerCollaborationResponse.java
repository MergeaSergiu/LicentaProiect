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
    private String firstName;
    private String lastName;
    private CollaborationStatus collaborationStatus;
}
