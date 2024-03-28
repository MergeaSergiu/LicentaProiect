package com.spring.project.mapper;

import com.spring.project.dto.UserDataResponse;
import com.spring.project.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserDataMapper {

    public UserDataResponse convertToDto(User user){
            return UserDataResponse.builder()
                    .id(Math.toIntExact(user.getId()))
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .role(user.getRole())
                    .build();
    }
}
