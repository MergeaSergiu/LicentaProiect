package com.spring.project.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserSubscriptionsDataResponse {

    private String subscriptionName;
    private Double subscriptionPrice;
    private String firstName;
    private String lastName;
    private LocalDate startDate;
    private LocalDate endDate;
}
