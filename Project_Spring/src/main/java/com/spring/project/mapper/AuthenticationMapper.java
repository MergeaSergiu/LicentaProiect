package com.spring.project.mapper;

import com.spring.project.dto.AuthenticationResponse;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationMapper {

    public AuthenticationResponse convertToDto(String jwt, String refresh_jwt, String role){
        return AuthenticationResponse.builder()
                .accessToken(jwt)
                .refreshToken(refresh_jwt)
                .user_Role(role)
                .build();
    }

}
