package com.spring.project.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
@Setter
@Table(name = "_subscription")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String subscriptionName;
    private Double subscriptionPrice;
    private Integer subscriptionTime;
    private String subscriptionDescription;

    public Subscription(String subscriptionName, Double subscriptionPrice, Integer subscriptionTime, String subscriptionDescription) {
        this.subscriptionName = subscriptionName;
        this.subscriptionPrice = subscriptionPrice;
        this.subscriptionTime = subscriptionTime;
        this.subscriptionDescription = subscriptionDescription;
    }


}
