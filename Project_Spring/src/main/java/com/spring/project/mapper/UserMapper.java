package com.spring.project.mapper;

import com.spring.project.dto.RegistrationRequest;
import com.spring.project.model.User;
import com.spring.project.model.Role;
import com.spring.project.service.impl.EmailValidator;
import com.spring.project.service.impl.PasswordValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserMapper {

    private EmailValidator emailValidator;
    private PasswordValidator passwordValidator;
    public User convertFromDto(RegistrationRequest registrationRequest, Role role){
        if(registrationRequest.getEmail() == null){
            throw new IllegalArgumentException("Email is not valid");
        }
        boolean isValidEmail = emailValidator.test(registrationRequest.getEmail());
        if(!isValidEmail){
            throw new IllegalArgumentException("Email do not respect the criteria");
        }
        if(registrationRequest.getPassword() == null) {
            throw new IllegalArgumentException("Password is not valid");
        }

        boolean isValidPassword = passwordValidator.test(registrationRequest.getPassword());
        if(!isValidPassword){
            throw new IllegalArgumentException("Password do not respect the criteria");
        }

        if(registrationRequest.getFirstName() == null || registrationRequest.getLastName() == null){
            throw new IllegalArgumentException("Names can not be null");
        }

        if(registrationRequest.getFirstName().equalsIgnoreCase("null") || registrationRequest.getLastName().equalsIgnoreCase("null")){
            throw new IllegalArgumentException("Names can not be name null");
        }

        return User.builder()
                .firstName(registrationRequest.getFirstName())
                .lastName(registrationRequest.getLastName())
                .email(registrationRequest.getEmail())
                .password(registrationRequest.getPassword())
                .role(role)
                .build();
    }
}
