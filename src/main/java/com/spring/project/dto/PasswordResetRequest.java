package com.spring.project.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@Setter
@Getter
public class PasswordResetRequest {

    private String email;
    private String newPassword;
    private String confirmedPassword;

}
