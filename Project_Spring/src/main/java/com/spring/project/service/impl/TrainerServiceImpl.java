package com.spring.project.service.impl;

import com.spring.project.dto.TrainingClassResponse;
import com.spring.project.model.Client;
import com.spring.project.model.TrainingClass;
import com.spring.project.service.TrainerService;
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

    @Autowired
    private final ClientService clientService;

    public List<TrainingClassResponse> getTrainingClassesForTrainer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Client trainer = clientService.findClientByEmail(authentication.getName());
            List<TrainingClass> trainingClasses = trainingClassServiceImpl.getTrainingClassesForTrainer(trainer.getId());
            if (trainingClasses != null) {
                return trainingClasses.stream()
                        .map(trainingClass -> TrainingClassResponse.builder()
                                .className(trainingClass.getClassName())
                                .localDate(trainingClass.getLocalDate())
                                .duration(trainingClass.getDuration())
                                .intensity(trainingClass.getIntensity())
                                .trainerId(trainer.getId())
                                .build())
                        .collect(Collectors.toList());
            } else {
                return new ArrayList<>();
            }
    }
}
