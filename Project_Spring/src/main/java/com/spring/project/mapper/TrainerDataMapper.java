package com.spring.project.mapper;

import com.spring.project.dto.TrainerResponse;
import com.spring.project.model.User;
import org.springframework.stereotype.Component;

@Component
public class TrainerDataMapper {

    public TrainerResponse convertToDto(User trainer){
        return TrainerResponse.builder()
                .id(Math.toIntExact(trainer.getId()))
                .firstName(trainer.getFirstName())
                .lastName(trainer.getLastName())
                .email(trainer.getEmail())
                .build();
    }
}
