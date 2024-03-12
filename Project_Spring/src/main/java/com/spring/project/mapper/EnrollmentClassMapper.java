package com.spring.project.mapper;

import com.spring.project.model.Client;
import com.spring.project.model.EnrollmentTrainingClass;
import com.spring.project.model.TrainingClass;
import org.springframework.stereotype.Component;

@Component
public class EnrollmentClassMapper {

    public EnrollmentTrainingClass createEnrollmentForUser(TrainingClass trainingClass, Client user){
        return EnrollmentTrainingClass
                .builder()
                .trainingClass(trainingClass)
                .user(user)
                .build();
    }
}
