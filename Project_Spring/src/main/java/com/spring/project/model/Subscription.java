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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String subscriptionName;
    private Double subscriptionPrice;
    private Integer subscriptionTime;
    private String subscriptionDescription;



}
