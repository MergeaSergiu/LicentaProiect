package com.spring.project.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class JwtRefreshToken {

    private String refreshToken;
    // Default constructor
    public JwtRefreshToken() {
    }

    // Constructor for deserialization
    public JwtRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
