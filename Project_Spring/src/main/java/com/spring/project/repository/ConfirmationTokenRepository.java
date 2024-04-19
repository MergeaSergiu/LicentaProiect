package com.spring.project.repository;

import com.spring.project.token.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {

    Optional<ConfirmationToken> findByToken(String token);

    @Query("UPDATE ConfirmationToken c " +
            "SET c.confirmedAt = ?2 " +
            "WHERE c.token = ?1 ")
    @Modifying
    @Transactional
    void updateConfirmedAt(String token, LocalDateTime now);

    @Modifying
    @Transactional
    void deleteByuser_Id(Long id);

    ConfirmationToken findByuser_Id(Long id);
}
