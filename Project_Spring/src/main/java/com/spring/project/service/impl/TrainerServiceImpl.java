package com.spring.project.service.impl;

import com.spring.project.dto.TrainingClassResponse;
import com.spring.project.mapper.TrainingClassMapper;
import com.spring.project.model.User;
import com.spring.project.model.TrainingClass;
import com.spring.project.repository.ClientRepository;
import com.spring.project.service.TrainerService;
import com.spring.project.util.UtilMethods;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TrainerServiceImpl implements TrainerService {

    @Autowired
    private final TrainingClassServiceImpl trainingClassServiceImpl;
    private final TrainingClassMapper trainingClassMapper;
    private final ClientRepository clientRepository;
    private final UtilMethods utilMethods;

    public List<TrainingClassResponse> getTrainingClassesForTrainer(String authorization) {
            String username = utilMethods.extractUsernameFromAuthorizationHeader(authorization);
            User trainer = clientRepository.findByEmail(username).orElse(null);
            if(trainer == null){
                throw new EntityNotFoundException("Trainer does not exist");
            }
            List<TrainingClass> trainingClasses = trainingClassServiceImpl.getTrainingClassesForTrainer(trainer.getId());
            if (trainingClasses != null) {
                return trainingClasses.stream()
                        .map(trainingClassMapper::convertToDto).collect(Collectors.toList());
            } else {
                return new ArrayList<>();
            }
    }
}
