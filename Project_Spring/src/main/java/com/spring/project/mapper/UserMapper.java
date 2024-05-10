package com.spring.project.mapper;

import com.spring.project.Exception.EmailNotAvailableException;
import com.spring.project.Exception.InvalidCredentialsException;
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
            throw new EmailNotAvailableException("Email is not valid");
        }
        boolean isValidEmail = emailValidator.test(registrationRequest.getEmail());
        if(!isValidEmail){
            throw new EmailNotAvailableException("Email do not respect the criteria");
        }
        if(registrationRequest.getPassword() == null) {
            throw new InvalidCredentialsException("Password is not valid");
        }

        boolean isValidPassword = passwordValidator.test(registrationRequest.getPassword());
        if(!isValidPassword){
            throw new InvalidCredentialsException("Password do not respect the criteria");
        }

        if(registrationRequest.getFirstName() == null || registrationRequest.getLastName() == null){
            throw new InvalidCredentialsException("Names can not be null");
        }

        if(registrationRequest.getFirstName().equalsIgnoreCase("null") || registrationRequest.getLastName().equalsIgnoreCase("null")){
            throw new InvalidCredentialsException("Names can not be name null");
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
