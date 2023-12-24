package com.spring.project.dto;

import jakarta.validation.constraints.NotBlank;
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

    private Double subscriptionPrice;

    private Integer subscriptionTime;

    private String subscriptionDescription;



}
