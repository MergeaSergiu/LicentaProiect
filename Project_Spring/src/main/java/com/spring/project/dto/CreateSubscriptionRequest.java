package com.spring.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateSubscriptionRequest {

    @NotBlank
    private String subscriptionName;

    @NotNull
    private Double subscriptionPrice;

    @NotBlank
    private String subscriptionDescription;
}
