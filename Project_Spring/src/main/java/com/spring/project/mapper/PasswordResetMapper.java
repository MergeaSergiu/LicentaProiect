package com.spring.project.mapper;

import com.spring.project.dto.PasswordResetResponse;
import org.springframework.stereotype.Component;

@Component
public class PasswordResetMapper {

    public PasswordResetResponse convertToDto(String message){
        return PasswordResetResponse.builder()
                .passwordResetResponse(message)
                .build();
    }

}
