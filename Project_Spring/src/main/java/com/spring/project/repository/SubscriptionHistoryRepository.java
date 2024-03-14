package com.spring.project.repository;

import com.spring.project.model.SubscriptionsHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SubscriptionHistoryRepository extends JpaRepository<SubscriptionsHistory, Integer> {
    List<SubscriptionsHistory> findAll();
    List<SubscriptionsHistory> findByuser_Id(@Param("id") Integer id);

    @Query("SELECT sh FROM SubscriptionsHistory sh " +
            "WHERE sh.user.id = :userId " +
            "AND sh.subscriptionEndTime >= :date " +
            "ORDER BY sh.subscriptionEndTime DESC")
    SubscriptionsHistory findActiveSubscriptionForUser(@Param("userId") Integer userId, @Param("date") LocalDate date);
}
