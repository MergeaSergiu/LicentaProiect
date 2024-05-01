package com.spring.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@Setter
@Getter
public class PasswordResetRequest {

    @NotBlank
    private String newPassword;

    @NotBlank
    private String confirmedPassword;

    @JsonProperty("token")
    private String token;
}
