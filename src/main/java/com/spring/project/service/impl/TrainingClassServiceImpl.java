package com.spring.project.service.impl;

import com.spring.project.model.TrainingClass;
import com.spring.project.repository.TrainingClassRepository;
import com.spring.project.service.TrainingClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingClassServiceImpl implements TrainingClassService {


    private final TrainingClassRepository trainingClassRepository;

    public void createTrainingClass(TrainingClass trainingClass){
        trainingClassRepository.save(trainingClass);
    }

    public TrainingClass getTrainingClassByName(String className){
        return trainingClassRepository.getTrainingClassByName(className);
    }

    public TrainingClass findById(Integer id) {
        return trainingClassRepository.findById(id).orElse(null);
    }

    public void deleteTrainingClass(Integer id) {
        trainingClassRepository.deleteById(id);
    }

    public List<TrainingClass> getTrainingClasses() {
        return trainingClassRepository.findAll();
    }
    public List<TrainingClass> getTrainingClassesForTrainer(Integer id) {
        return trainingClassRepository.findAllByTrainer_Id(id);
    }
}
