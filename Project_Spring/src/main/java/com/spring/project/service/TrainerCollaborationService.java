package com.spring.project.service;

import com.spring.project.dto.TrainerCollaborationResponse;

import java.util.List;

public interface TrainerCollaborationService {

    void sendCollaborationRequest(Long trainerId);

    List<TrainerCollaborationResponse> getCollaborationForTrainer();

    void acceptUserCollaboration(Long collaborationId);

    void declineUserCollaboration(Long collaborationId);

    void finishCollaboration(Long collaborationId);

}
