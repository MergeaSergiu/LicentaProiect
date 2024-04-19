package com.spring.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="_subscriptionsHistory")
public class SubscriptionsHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @NotBlank
    private String subscriptionName;

    @NotNull
    private Double subscriptionPrice;

    @NotNull
    private LocalDate subscriptionStartTime;

    @NotNull
    private LocalDate subscriptionEndTime;

}
