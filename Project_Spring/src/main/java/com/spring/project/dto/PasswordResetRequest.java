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

    @NotBlank(message = "The password can not be blank")
    private String newPassword;

    @NotBlank(message = "The password can not be blank")
    private String confirmedPassword;

    @JsonProperty("token")
    private String token;
}
