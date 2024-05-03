package com.spring.project.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class UserSubscriptionRequest {

    @NotNull
    private Integer subscriptionId;
    @NotNull
    private Integer userId;
}
