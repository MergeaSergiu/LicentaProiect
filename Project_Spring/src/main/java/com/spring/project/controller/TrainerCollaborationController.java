package com.spring.project.controller;

import com.spring.project.dto.TrainerCollaborationResponse;
import com.spring.project.service.TrainerCollaborationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping(path= "project/api/v1/collaboration/users")
public class TrainerCollaborationController {

    @Autowired
    private final TrainerCollaborationService trainerCollaborationService;

    @PostMapping("/{trainerId}")
    public ResponseEntity<Void> sendCollabRequest(@PathVariable("trainerId") Long trainerId){
        trainerCollaborationService.sendCollaborationRequest(trainerId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/trainers")
    public ResponseEntity<List<TrainerCollaborationResponse>> getCollaborationsForTrainer(){
        List<TrainerCollaborationResponse> trainerCollaborationResponses = trainerCollaborationService.getCollaborationForTrainer();
        return new ResponseEntity<>(trainerCollaborationResponses, HttpStatus.OK);
    }

    @PutMapping("/trainers/{collaborationId}")
    public ResponseEntity<Void> acceptUserCollaboration(@PathVariable("collaborationId") Long collaborationId){
        trainerCollaborationService.acceptUserCollaboration(collaborationId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/trainers/{collaborationId}")
    public ResponseEntity<Void> declineUserCollaboration(@PathVariable("collaborationId") Long collaborationId){
        trainerCollaborationService.declineUserCollaboration(collaborationId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/trainers/ended/{collaborationId}")
    public ResponseEntity<Void> finishCollaboration(@PathVariable("collaborationId") Long collaborationId){
        trainerCollaborationService.finishCollaboration(collaborationId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
