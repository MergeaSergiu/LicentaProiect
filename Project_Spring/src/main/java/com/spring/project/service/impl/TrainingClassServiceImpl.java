package com.spring.project.service.impl;

import com.spring.project.dto.TrainingClassRequest;
import com.spring.project.dto.TrainingClassResponse;
import com.spring.project.mapper.TrainingClassMapper;
import com.spring.project.model.TrainingClass;
import com.spring.project.model.User;
import com.spring.project.repository.UserRepository;
import com.spring.project.repository.EnrollmentTrainingClassRepository;
import com.spring.project.repository.TrainingClassRepository;
import com.spring.project.service.TrainingClassService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainingClassServiceImpl implements TrainingClassService {


    private final TrainingClassRepository trainingClassRepository;
    private final TrainingClassMapper trainingClassMapper;
    private final EnrollmentTrainingClassRepository enrollmentTrainingClassRepository;
    private final UserRepository userRepository;

    public void createTrainingClass(TrainingClassRequest trainingClassRequest) {
        User trainer = userRepository.findById(Long.valueOf(trainingClassRequest.getTrainerId())).orElseThrow(() -> new EntityNotFoundException("Trainer does not exist"));
        if (!trainer.getRole().getName().equals("TRAINER")) {
            throw new EntityNotFoundException("User does not have 'TRAINER' role");
        }
        TrainingClass trainingClass = trainingClassMapper.convertFromDto(trainingClassRequest, trainer);
        trainingClassRepository.save(trainingClass);
    }

    public TrainingClassResponse findById(Long id) {
        TrainingClass trainingClass = trainingClassRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Training Class does not exist"));
        return trainingClassMapper.convertToDto(trainingClass);
    }

    public void deleteTrainingClass(Long id) {
        TrainingClass trainingClass = trainingClassRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Training Class does not exist"));
        enrollmentTrainingClassRepository.deleteAllByTrainingClass_Id(trainingClass.getId());
        trainingClassRepository.deleteById(id);
    }

    @Override
    public void updateTrainingClass(Long id, TrainingClassRequest trainingClassRequest) {
        TrainingClass trainingClass = trainingClassRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Training Class does not exist"));

        if (trainingClassRepository.getTrainingClassByName(trainingClassRequest.getClassName()) != null && !trainingClassRequest.getClassName().equals(trainingClass.getClassName())) {
            throw new EntityExistsException("There is already already a class with this name");
        }
        User trainer = userRepository.findById(Long.valueOf(trainingClassRequest.getTrainerId())).orElseThrow(() -> new EntityNotFoundException("Trainer does not exist"));
        if (!trainer.getRole().getName().equals("TRAINER")) {
            throw new EntityNotFoundException("User does not have 'TRAINER' role");
        }
        trainingClass.setClassName(trainingClassRequest.getClassName());
        trainingClass.setIntensity(trainingClassRequest.getIntensity());
        trainingClass.setStartTime(trainingClass.getStartTime());
        trainingClass.setDuration(trainingClassRequest.getDuration());
        trainingClass.setLocalDate(trainingClassRequest.getLocalDate());
        trainingClass.setTrainer(trainer);
        trainingClassRepository.save(trainingClass);
    }

    public List<TrainingClassResponse> getTrainingClasses() {
        return trainingClassRepository.findAll().stream()
                .map(trainingClassMapper::convertToDto)
                .collect(Collectors.toList());
    }

    public List<TrainingClass> getTrainingClassesForTrainer(Long id) {
        User trainer = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Trainer does not exit"));
        return trainingClassRepository.findAllByTrainer_Id(trainer.getId());
    }

}
