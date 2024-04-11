package com.spring.project.service;

import com.spring.project.dto.TrainingClassRequest;
import com.spring.project.dto.TrainingClassResponse;
import com.spring.project.model.TrainingClass;

import java.util.List;

public interface TrainingClassService {

    void createTrainingClass(TrainingClassRequest trainingClass, String authorization);

    TrainingClassResponse findById(Long id);

    void deleteTrainingClass(Long id);

    void updateTrainingClass(Long id, TrainingClassRequest trainingClassRequest);

    List<TrainingClassResponse> getTrainingClasses();
    List<TrainingClass> getTrainingClassesForTrainer(Long id);
}
