package com.spring.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class EnrollUserRequest {

    @NotBlank(message = "emailAddress can not be blank")
    @NotNull
    private String emailAddress;

    @NotBlank(message = "trainingClass Name can not be blak")
    @NotNull
    private String trainingClassName;

}
