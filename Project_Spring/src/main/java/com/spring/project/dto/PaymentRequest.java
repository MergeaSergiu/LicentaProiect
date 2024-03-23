package com.spring.project.dto;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class PaymentRequest {

    private String cardHolderName;
    private int amount;
    private String currency;

}
