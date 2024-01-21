package com.spring.project.service;

import com.spring.project.model.TrainingClass;

import java.util.List;

public interface TrainingClassService {

    void createTrainingClass(TrainingClass trainingClass);

    TrainingClass getTrainingClassByName(String className);

    TrainingClass findById(Integer id);

    void deleteTrainingClass(String className);

    List<TrainingClass> getTrainingClasses();
    List<TrainingClass> getTrainingClassesForTrainer(Integer id);
}
