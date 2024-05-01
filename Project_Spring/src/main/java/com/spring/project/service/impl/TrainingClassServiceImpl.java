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
import com.spring.project.util.UtilMethods;
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
    private final UtilMethods utilMethods;
    private final EmailSenderImpl emailService;

    public void createTrainingClass(TrainingClassRequest trainingClassRequest, String authorization){
            String username = utilMethods.extractUsernameFromAuthorizationHeader(authorization);
            User user = userRepository.findByEmail(username).orElse(null);
            if(user == null){
                throw new EntityNotFoundException("User does not exist");
            }

            User trainer = userRepository.findById(Long.valueOf(trainingClassRequest.getTrainerId())).orElse(null);
            if(trainer == null){
                throw new EntityNotFoundException("Trainer does not exist");
            }
            TrainingClass trainingClass = trainingClassMapper.convertFromDto(trainingClassRequest, trainer);
            trainingClassRepository.save(trainingClass);
            String emailTemplate = utilMethods.loadEmailTemplateFromResource("trainingClassCreated.html");
            emailTemplate = emailTemplate.replace("${user}", user.getFirstName()+" "+ user.getLastName());
            emailTemplate = emailTemplate.replace("${trainingClass}", trainingClassRequest.getClassName());
            emailService.send(username, emailTemplate, "Training class was created");
    }

    public TrainingClassResponse findById(Long id) {
        TrainingClass trainingClass =  trainingClassRepository.findById(id).orElse(null);
        if(trainingClass == null){
            throw new EntityNotFoundException("Training Class does not exist");
        }
        return trainingClassMapper.convertToDto(trainingClass);
    }

    public void deleteTrainingClass(Long id) {
        TrainingClass trainingClass = trainingClassRepository.findById(id).orElse(null);
        if(trainingClass == null){
            throw new EntityNotFoundException("Training Class does not exist");
        }
        enrollmentTrainingClassRepository.deleteAllByTrainingClass_Id(id);
        trainingClassRepository.deleteById(id);
    }

    @Override
    public void updateTrainingClass(Long id, TrainingClassRequest trainingClassRequest) {
        TrainingClass trainingClass = trainingClassRepository.findById(id).orElse(null);
        if(trainingClass == null){
            throw new EntityNotFoundException("Training Class does not exist");
        }

        if(trainingClassRepository.getTrainingClassByName(trainingClassRequest.getClassName()) != null && !trainingClassRequest.getClassName().equals(trainingClass.getClassName())){
            throw new EntityExistsException("There is already already a class with this name");
        }
        User trainer = userRepository.findById(Long.valueOf(trainingClassRequest.getTrainerId())).orElse(null);
        if(trainer == null){
            throw new EntityNotFoundException("Trainer does not exist");
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
        User trainer = userRepository.findById(id).orElse(null);
        if(trainer == null){
            throw new EntityNotFoundException("Trainer does not exist");
        }
        return trainingClassRepository.findAllByTrainer_Id(id);
    }

}
