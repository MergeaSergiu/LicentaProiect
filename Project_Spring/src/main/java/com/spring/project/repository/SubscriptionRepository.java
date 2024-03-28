package com.spring.project.repository;

import com.spring.project.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Query("SELECT s From Subscription s WHERE s.subscriptionName = :subscriptionName")
    Subscription findBySubscriptionName(String subscriptionName);

}
