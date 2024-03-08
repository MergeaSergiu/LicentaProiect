package com.spring.project.mapper;

import com.spring.project.dto.TrainerResponse;
import com.spring.project.model.Client;
import org.springframework.stereotype.Component;

@Component
public class TrainerDataMapper {

    public TrainerResponse convertToDto(Client trainer){
        return TrainerResponse.builder()
                .id(trainer.getId())
                .firstName(trainer.getFirstName())
                .lastName(trainer.getLastName())
                .build();
    }
}
