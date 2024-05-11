package com.spring.project.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class PaymentRequest {

    @NotBlank
    private String cardHolderName;

    @NotNull
    private Integer amount;

    @NotBlank
    private String currency;

    @NotNull
    private Long subscriptionId;

}
