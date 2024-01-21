package com.spring.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {

    @NotBlank(message = "The first name can not be blank")
    private String firstName;

    @NotBlank(message = "The last name can not be blank")
    private String lastName;

    @NotBlank(message = "The email can not be blank")
    private String email;

    @NotBlank(message = "The password can not be blank")
    private String password;

    @NotNull
    private boolean isAdmin;

}
