package com.spring.project.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Data
@Getter
@Setter
public class StripeChargeRequest {

    private String stripeToken;
    private String username;
    private Boolean success;
    private String message;
    private String chargedId;
    private Map<String, Object> additionalInfo = new HashMap<>();
}
