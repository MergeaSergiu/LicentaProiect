package com.spring.project.repository;

import com.spring.project.model.SubscriptionPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionPaymentRespository extends JpaRepository<SubscriptionPayment, Integer> {
}
