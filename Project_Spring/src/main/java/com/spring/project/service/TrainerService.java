package com.spring.project.service;

import com.spring.project.dto.TrainingClassResponse;
import com.spring.project.model.TrainingClass;

import java.util.List;

public interface TrainerService {

    List<TrainingClassResponse> getTrainingClassesForTrainer(String authorization);

}
