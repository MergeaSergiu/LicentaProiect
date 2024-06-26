package com.spring.project.service.impl;

import com.spring.project.dto.TrainingClassResponse;
import com.spring.project.mapper.EnrollmentClassMapper;
import com.spring.project.mapper.TrainingClassMapper;
import com.spring.project.model.EnrollmentTrainingClass;
import com.spring.project.model.TrainingClass;
import com.spring.project.model.User;
import com.spring.project.repository.EnrollmentTrainingClassRepository;
import com.spring.project.repository.TrainingClassRepository;
import com.spring.project.service.EnrollmentTrainingClassService;
import com.spring.project.util.UtilMethods;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentTrainingClassServiceImpl implements EnrollmentTrainingClassService {

    private final EnrollmentTrainingClassRepository enrollmentTrainingClassRepository;
    private final UtilMethods utilMethods;
    private final TrainingClassRepository trainingClassRepository;
    private final TrainingClassMapper trainingClassMapper;
    private final EnrollmentClassMapper enrollmentClassMapper;
    private final EmailSenderImpl emailService;


    @Override
    public void saveEnrollmentAction(Long trainingClassId, String authorization) {

        User user = utilMethods.extractUsernameFromAuthorizationHeader(authorization);
        TrainingClass trainingClass = trainingClassRepository.findById(trainingClassId).orElseThrow(() -> new EntityNotFoundException("Training Class does not exist"));
        EnrollmentTrainingClass enrollmentTrainingClassForUser = enrollmentTrainingClassRepository.findByTrainingClass_IdAndUser_Id(trainingClassId, user.getId());
        if (enrollmentTrainingClassForUser != null) {
            throw new EntityExistsException("User already enrolled to this class");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (LocalDate.parse(trainingClass.getLocalDate(), formatter).isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Can not create a training class in past");
        }
        EnrollmentTrainingClass enrollmentTrainingClass = enrollmentClassMapper.createEnrollmentForUser(trainingClass, user);
        enrollmentTrainingClassRepository.save(enrollmentTrainingClass);
        String emailTemplate = utilMethods.loadEmailTemplateFromResource("enrollClassEmail.html");
        emailTemplate = emailTemplate.replace("${user}", user.getFirstName() + " " + user.getLastName());
        emailTemplate = emailTemplate.replace("${enrollClass}", trainingClass.getClassName());
        emailService.send(user.getEmail(), emailTemplate, "Thank you for joining the class");
    }


    @Override
    public List<TrainingClassResponse> getClassesForUser(String authorization) {

        User user = utilMethods.extractUsernameFromAuthorizationHeader(authorization);
        List<EnrollmentTrainingClass> enrollmentTrainingClasses = enrollmentTrainingClassRepository.findAllByuser_Id(user.getId());
        if (enrollmentTrainingClasses == null) {
            return null;
        }
        List<TrainingClassResponse> enrollClassResponses = new ArrayList<>();
        for (EnrollmentTrainingClass enrollmentTrainingClass : enrollmentTrainingClasses) {
            TrainingClass trainingClass = trainingClassRepository.findById(enrollmentTrainingClass.getTrainingClass().getId()).orElse(null);
            if (trainingClass == null) {
                throw new EntityNotFoundException("Training Class does not exist");
            }
            enrollClassResponses.add(trainingClassMapper.convertToDto(trainingClass));
        }
        return enrollClassResponses;
    }

    @Override
    public void deleteEnrollmentForUser(Long trainingClassId, String authorization) {

        User user = utilMethods.extractUsernameFromAuthorizationHeader(authorization);
        TrainingClass trainingClass = trainingClassRepository.findById(trainingClassId).orElseThrow(() -> new EntityNotFoundException("Training Class does not exist"));
        EnrollmentTrainingClass enrollmentTrainingClass = enrollmentTrainingClassRepository.findByTrainingClass_IdAndUser_Id(trainingClass.getId(), user.getId());
        if (enrollmentTrainingClass == null) {
            throw new EntityNotFoundException("Enrollment does not exist for the user");
        }

        int compare = Long.compare(enrollmentTrainingClass.getUser().getId(), user.getId());
        if (compare != 0) {
            throw new IllegalArgumentException("User tried to delete other user enrollment");
        }
        enrollmentTrainingClassRepository.deleteByTrainingClassIdAndUserId(trainingClassId, user.getId());
    }

}
