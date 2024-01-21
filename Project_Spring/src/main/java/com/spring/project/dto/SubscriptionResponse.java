package com.spring.project.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Setter
@Getter
public class SubscriptionResponse {
    private String subscriptionName;
    private Double subscriptionPrice;
    private Integer subscriptionTime;
    private String subscriptionDescription;
}
