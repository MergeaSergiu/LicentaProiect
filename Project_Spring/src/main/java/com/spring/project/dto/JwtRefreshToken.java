package com.spring.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class JwtRefreshToken {

    @JsonProperty("refreshToken")
    private String refreshToken;
    // Default constructor
    public JwtRefreshToken() {
    }

    // Constructor for deserialization
    public JwtRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
