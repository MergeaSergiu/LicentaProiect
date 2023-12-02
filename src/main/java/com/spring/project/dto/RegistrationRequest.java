package com.spring.project.dto;

import com.spring.project.model.ClientRole;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private ClientRole clientRole;

}
