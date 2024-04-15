package com.spring.project.service;

import com.spring.project.dto.TrainerCollaborationResponse;

import java.util.List;

public interface TrainerCollaborationService {

    void sendCollaborationRequest(Long trainerId, String authorization);

    List<TrainerCollaborationResponse> getCollaborationForTrainer(String authorization);

    void acceptUserCollaboration(Long collaborationId);

    void declineUserCollaboration(Long collaborationId, String authorization);

    void finishCollaboration(Long collaborationId);

    List<TrainerCollaborationResponse> getCollaborationForUser(String authorization);

}
