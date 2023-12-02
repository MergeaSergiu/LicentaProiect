package com.spring.project.config;

import lombok.*;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class AuthenticationResponse {

    private String token;

}
