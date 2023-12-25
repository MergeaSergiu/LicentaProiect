package com.spring.project.service;

import com.spring.project.Exception.CustomExpiredJwtException;
import com.spring.project.model.Client;
import com.spring.project.model.TrainingClass;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TrainerService {

    @Autowired
    private final TrainingClassService trainingClassService;

    @Autowired
    private final ClientService clientService;


    public List<TrainingClass> getTrainingClassesForTrainer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            Client trainer = clientService.findClientByEmail(authentication.getName());
            Integer id = trainer.getId();
            List<TrainingClass> trainingClasses = trainingClassService.getTrainingClassesForTrainer(id);
            return trainingClasses;
        }else{
            throw new CustomExpiredJwtException("Session expired");
        }
    }
}
