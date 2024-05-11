package com.spring.project.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegistrationRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
