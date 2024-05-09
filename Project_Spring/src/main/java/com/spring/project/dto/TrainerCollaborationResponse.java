package com.spring.project.dto;

import com.spring.project.model.CollaborationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class TrainerCollaborationResponse {

    private Long collaborationId;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate startTime;
    private LocalDate endTime;
    private CollaborationStatus collaborationStatus;
}
