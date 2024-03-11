package com.spring.project.mapper;

import com.spring.project.dto.UserDataResponse;
import com.spring.project.model.Client;
import org.springframework.stereotype.Component;

@Component
public class UserDataMapper {

    public UserDataResponse convertToDto(Client user){
            return UserDataResponse.builder()
                    .id(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .role(user.getRole())
                    .build();
    }
}
