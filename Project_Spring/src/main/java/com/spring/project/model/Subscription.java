package com.spring.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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

    @NotBlank(message = "SubscriptionName should not be empty")
    @Pattern(regexp = "^[a-zA-Z]+(\\s[a-zA-Z]+)*$", message = "Subscription name must contain only letters")
    private String subscriptionName;

    @NotNull(message = "SubscriptionPrice should not be null")
    private Double subscriptionPrice;

    @NotBlank(message = "Subscription Description should not be empty")
    private String subscriptionDescription;



}
