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

    @NotNull(message = "You need to add a price")
    private Double subscriptionPrice;

    @NotNull(message = "You need to add a time")
    private Integer subscriptionTime;

    @NotNull
    @NotBlank(message = "Subscription Description can not be blank")
    private String subscriptionDescription;
}
