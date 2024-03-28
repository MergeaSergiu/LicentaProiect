package com.spring.project.service;

import com.spring.project.model.TrainingClass;

import java.util.List;

public interface TrainingClassService {

    void createTrainingClass(TrainingClass trainingClass);

    TrainingClass getTrainingClassByName(String className);

    TrainingClass findById(Long id);

    void deleteTrainingClass(Long id);

    List<TrainingClass> getTrainingClasses();
    List<TrainingClass> getTrainingClassesForTrainer(Long id);
}
