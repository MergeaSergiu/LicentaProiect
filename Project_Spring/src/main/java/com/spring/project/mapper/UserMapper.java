package com.spring.project.mapper;

import com.spring.project.dto.RegistrationRequest;
import com.spring.project.model.User;
import com.spring.project.model.Role;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User convertFromDto(RegistrationRequest registrationRequest, Role role){
        return User.builder()
                .firstName(registrationRequest.getFirstName())
                .lastName(registrationRequest.getLastName())
                .email(registrationRequest.getEmail())
                .password(registrationRequest.getPassword())
                .role(role)
                .build();
    }
}