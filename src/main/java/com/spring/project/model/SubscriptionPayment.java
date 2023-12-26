package com.spring.project.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "_subPayment")
public class SubscriptionPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Integer id;

    String subscriptionName;

    Double subscriptionPrice;

    Boolean charged;
    @OneToOne
    @JoinColumn(name = "client_id")
    Client client;

    public SubscriptionPayment(String subscriptionName, Double subscriptionPrice, Boolean charged, Client client){
        this.subscriptionName = subscriptionName;
        this.subscriptionPrice = subscriptionPrice;
        this.charged = charged;
        this.client = client;
    }


}
