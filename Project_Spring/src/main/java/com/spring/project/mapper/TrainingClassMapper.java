package com.spring.project.mapper;

import com.spring.project.dto.TrainingClassRequest;
import com.spring.project.dto.TrainingClassResponse;
import com.spring.project.model.User;
import com.spring.project.model.TrainingClass;
import org.springframework.stereotype.Component;

@Component
public class TrainingClassMapper {

    public TrainingClassResponse convertToDto(TrainingClass trainingClass){
        return TrainingClassResponse.builder()
                .id(Math.toIntExact(trainingClass.getId()))
                .className(trainingClass.getClassName())
                .duration(trainingClass.getDuration())
                .intensity(trainingClass.getIntensity())
                .localDate(trainingClass.getLocalDate())
                .startTime(trainingClass.getStartTime())
                .trainerId(Math.toIntExact(trainingClass.getTrainer().getId()))
                .lastName(trainingClass.getTrainer().getLastName())
                .firstName(trainingClass.getTrainer().getFirstName())
                .build();
    }

    public TrainingClass convertFromDto(TrainingClassRequest trainingClassRequest, User trainer){
        return TrainingClass.builder()
                .className(trainingClassRequest.getClassName())
                .duration(trainingClassRequest.getDuration())
                .startTime(trainingClassRequest.getStartTime())
                .intensity(trainingClassRequest.getIntensity())
                .localDate(trainingClassRequest.getLocalDate())
                .trainer(trainer)
                .build();
    }
}
