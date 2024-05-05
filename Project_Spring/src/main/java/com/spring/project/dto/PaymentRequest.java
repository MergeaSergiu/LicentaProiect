package com.spring.project.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    private int amount;

    @NotBlank
    private String currency;

    private Long subscriptionId;

}
