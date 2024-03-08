package com.spring.project.mapper;

import com.spring.project.dto.TrainingClassResponse;
import com.spring.project.model.TrainingClass;
import org.springframework.stereotype.Component;

@Component
public class TrainingClassMapper {

    public TrainingClassResponse convertToDto(TrainingClass trainingClass){
        return TrainingClassResponse.builder()
                .id(trainingClass.getId())
                .className(trainingClass.getClassName())
                .duration(trainingClass.getDuration())
                .intensity(trainingClass.getIntensity())
                .localDate(trainingClass.getLocalDate())
                .startTime(trainingClass.getStartTime())
                .trainerId(trainingClass.getTrainer().getId())
                .lastName(trainingClass.getTrainer().getLastName())
                .firstName(trainingClass.getTrainer().getFirstName())
                .build();
    }
}
