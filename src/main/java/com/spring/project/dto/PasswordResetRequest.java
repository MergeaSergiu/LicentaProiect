package com.spring.project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@Setter
@Getter
public class PasswordResetRequest {
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @NotBlank(message = "The password can not be blank")
    private String newPassword;

    private String confirmedPassword;
}
