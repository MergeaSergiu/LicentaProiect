package com.spring.project.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class UserSubscriptionRequest {

    private Integer subscriptionId;
    private Integer userId;
}
