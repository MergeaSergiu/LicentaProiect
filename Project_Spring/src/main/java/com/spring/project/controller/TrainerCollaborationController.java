package com.spring.project.controller;

import com.spring.project.dto.TrainerCollaborationResponse;
import com.spring.project.service.TrainerCollaborationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping(path= "project/api/v1/collaboration/users")
public class TrainerCollaborationController {

    @Autowired
    private final TrainerCollaborationService trainerCollaborationService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{trainerId}")
    public ResponseEntity<Void> sendCollabRequest(@PathVariable("trainerId") Long trainerId, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        trainerCollaborationService.sendCollaborationRequest(trainerId, authorization);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasRole('TRAINER')")
    @GetMapping("/trainers")
    public ResponseEntity<List<TrainerCollaborationResponse>> getCollaborationsForTrainer(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        List<TrainerCollaborationResponse> trainerCollaborationResponses = trainerCollaborationService.getCollaborationForTrainer(authorization);
        return new ResponseEntity<>(trainerCollaborationResponses, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<List<TrainerCollaborationResponse>> getCollaborationForUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        List<TrainerCollaborationResponse> userCollaborationResponses = trainerCollaborationService.getCollaborationForUser(authorization);
        return new ResponseEntity<>(userCollaborationResponses, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('TRAINER')")
    @PutMapping("/trainers/{collaborationId}")
    public ResponseEntity<Void> acceptUserCollaboration(@PathVariable("collaborationId") Long collaborationId){
        trainerCollaborationService.acceptUserCollaboration(collaborationId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PreAuthorize("hasRole('TRAINER') or hasRole('USER')")
    @DeleteMapping("/trainers/{collaborationId}")
    public ResponseEntity<Void> declineUserCollaboration(@PathVariable("collaborationId") Long collaborationId, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        trainerCollaborationService.declineUserCollaboration(collaborationId, authorization);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PreAuthorize("hasRole('TRAINER')")
    @PutMapping("/trainers/ended/{collaborationId}")
    public ResponseEntity<Void> finishCollaboration(@PathVariable("collaborationId") Long collaborationId){
        trainerCollaborationService.finishCollaboration(collaborationId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
