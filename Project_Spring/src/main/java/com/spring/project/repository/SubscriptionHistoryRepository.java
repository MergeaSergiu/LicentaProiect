package com.spring.project.repository;

import com.spring.project.model.SubscriptionsHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface SubscriptionHistoryRepository extends JpaRepository<SubscriptionsHistory, Long> {
    @Query("SELECT sh FROM SubscriptionsHistory sh " +
            "WHERE sh.user.id = :userId " +
            "ORDER BY sh.subscriptionEndTime DESC")
    List<SubscriptionsHistory> findByUser_IdOrderBySubscriptionEndTimeAsc(@Param("userId") Long userId);

    @Query("SELECT sh FROM SubscriptionsHistory sh " +
            "WHERE sh.user.id = :userId " +
            "AND sh.subscriptionEndTime >= :date " +
            "ORDER BY sh.subscriptionEndTime DESC")
    SubscriptionsHistory findActiveSubscriptionForUser(@Param("userId") Long userId, @Param("date") LocalDate date);

    @Transactional
    @Modifying
    void deleteAllByUser_Id(@Param("userId") Long userId);
}
