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

    @NotBlank(message = "Subscription Title can not be blank")
    private String subscriptionName;

    @NotBlank(message = "Select a price for the subscription")
    @NotNull
    private Double subscriptionPrice;

    @NotBlank
    @NotNull
    private Integer subscriptionTime;

    @NotNull
    @NotBlank
    private String subscriptionDescription;


}
