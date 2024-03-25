package com.spring.project.dto;

import lombok.*;

@Data
@Builder
@Getter
@Setter
public class JwtRefreshToken {

    public JwtRefreshToken() {
    }

    public JwtRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    private  String refreshToken;
}
