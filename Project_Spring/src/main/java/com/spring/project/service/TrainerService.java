package com.spring.project.service;

import com.spring.project.dto.TrainingClassResponse;

import java.util.List;

public interface TrainerService {

    List<TrainingClassResponse> getTrainingClassesForTrainer(String authorization);

}
