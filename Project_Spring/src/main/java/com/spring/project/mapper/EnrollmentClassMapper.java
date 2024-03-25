package com.spring.project.mapper;

import com.spring.project.model.User;
import com.spring.project.model.EnrollmentTrainingClass;
import com.spring.project.model.TrainingClass;
import org.springframework.stereotype.Component;

@Component
public class EnrollmentClassMapper {

    public EnrollmentTrainingClass createEnrollmentForUser(TrainingClass trainingClass, User user){
        return EnrollmentTrainingClass
                .builder()
                .trainingClass(trainingClass)
                .user(user)
                .build();
    }
}
