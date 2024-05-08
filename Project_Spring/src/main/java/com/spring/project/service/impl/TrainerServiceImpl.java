package com.spring.project.service.impl;

import com.spring.project.dto.TrainingClassResponse;
import com.spring.project.mapper.TrainingClassMapper;
import com.spring.project.model.User;
import com.spring.project.model.TrainingClass;
import com.spring.project.service.TrainerService;
import com.spring.project.util.UtilMethods;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TrainerServiceImpl implements TrainerService {

    private final TrainingClassServiceImpl trainingClassServiceImpl;
    private final TrainingClassMapper trainingClassMapper;
    private final UtilMethods utilMethods;

    public List<TrainingClassResponse> getTrainingClassesForTrainer(String authorization) {
        User trainer = utilMethods.extractUsernameFromAuthorizationHeader(authorization);
        List<TrainingClass> trainingClasses = trainingClassServiceImpl.getTrainingClassesForTrainer(trainer.getId());
        if (trainingClasses != null) {
            return trainingClasses.stream()
                    .map(trainingClassMapper::convertToDto).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }
}
