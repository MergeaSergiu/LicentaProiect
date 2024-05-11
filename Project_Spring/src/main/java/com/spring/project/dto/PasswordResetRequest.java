package com.spring.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@Setter
@Getter
public class PasswordResetRequest {
    private String newPassword;

    private String confirmedPassword;

    @JsonProperty("token")
    private String token;
}
