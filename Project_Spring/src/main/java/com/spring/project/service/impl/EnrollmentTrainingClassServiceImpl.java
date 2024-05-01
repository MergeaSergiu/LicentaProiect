package com.spring.project.service.impl;

import com.spring.project.dto.TrainingClassResponse;
import com.spring.project.mapper.EnrollmentClassMapper;
import com.spring.project.mapper.TrainingClassMapper;
import com.spring.project.model.EnrollmentTrainingClass;
import com.spring.project.model.TrainingClass;
import com.spring.project.model.User;
import com.spring.project.repository.UserRepository;
import com.spring.project.repository.EnrollmentTrainingClassRepository;
import com.spring.project.repository.TrainingClassRepository;
import com.spring.project.service.EnrollmentTrainingClassService;
import com.spring.project.util.UtilMethods;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentTrainingClassServiceImpl implements EnrollmentTrainingClassService {

    @Autowired
    private final EnrollmentTrainingClassRepository enrollmentTrainingClassRepository;
    private final UtilMethods utilMethods;
    private final TrainingClassRepository trainingClassRepository;
    private final UserRepository userRepository;
    private final TrainingClassMapper trainingClassMapper;
    private final EnrollmentClassMapper enrollmentClassMapper;
    private final EmailSenderImpl emailService;


    @Override
    public void saveEnrollmentAction(Long trainingClassId, String authorization){
            String username = utilMethods.extractUsernameFromAuthorizationHeader(authorization);
            User user = userRepository.findByEmail(username).orElse(null);
            if(user == null){
            throw new EntityNotFoundException("User does not exist");
            }

            TrainingClass trainingClass = trainingClassRepository.findById(trainingClassId).orElse(null);
            if(trainingClass == null){
                throw new EntityNotFoundException("Training Class does not exist");
            }

            EnrollmentTrainingClass enrollmentTrainingClassForUser = enrollmentTrainingClassRepository.findByTrainingClass_IdAndUser_Id(trainingClassId, user.getId());
            if(enrollmentTrainingClassForUser != null) {
                throw new EntityExistsException("User already enrolled to this class");
            }
                EnrollmentTrainingClass enrollmentTrainingClass = enrollmentClassMapper.createEnrollmentForUser(trainingClass, user);
                enrollmentTrainingClassRepository.save(enrollmentTrainingClass);
                String emailTemplate = utilMethods.loadEmailTemplateFromResource("enrollClassEmail.html");
                emailTemplate = emailTemplate.replace("${user}", user.getFirstName()+" " + user.getLastName());
                emailTemplate = emailTemplate.replace("${enrollClass}", trainingClass.getClassName());
                emailService.send(username, emailTemplate, "Thank you for joining the class");
        }


    @Override
    public List<TrainingClassResponse> getClassesForUser(String authorization) {
        String username = utilMethods.extractUsernameFromAuthorizationHeader(authorization);
        User user = userRepository.findByEmail(username).orElse(null);
        if(user == null){
            throw new EntityNotFoundException("User does not exist");
        }
        List<EnrollmentTrainingClass> enrollmentTrainingClasses = enrollmentTrainingClassRepository.findAllByuser_Id(user.getId());
        if(enrollmentTrainingClasses == null){
            return null;
        }
            List<TrainingClassResponse> enrollClassResponses = new ArrayList<>();
            for(EnrollmentTrainingClass enrollmentTrainingClass : enrollmentTrainingClasses){
                TrainingClass trainingClass = trainingClassRepository.findById(enrollmentTrainingClass.getTrainingClass().getId()).orElse(null);
                if(trainingClass == null){
                    throw new EntityNotFoundException("Training Class does not exist");
                }
                enrollClassResponses.add(trainingClassMapper.convertToDto(trainingClass));
            }
            return enrollClassResponses;
    }

    @Override
    public void deleteEnrollmentForUser(Long trainingClassId, String authorization) {
        String username = utilMethods.extractUsernameFromAuthorizationHeader(authorization);
        User user = userRepository.findByEmail(username).orElse(null);
        if(user == null) {
            throw new EntityNotFoundException("User does not exist");
        }
        TrainingClass trainingClass = trainingClassRepository.findById(trainingClassId).orElse(null);
        if(trainingClass == null){
            throw new EntityNotFoundException("Training Class does not exist");
        }
        enrollmentTrainingClassRepository.deleteByTrainingClassIdAndUserId(trainingClassId,user.getId());

    }

}
