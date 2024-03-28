package com.spring.project.repository;

import com.spring.project.token.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    @Modifying
    @Transactional
    void deleteByuser_Id(Long id);

    Optional<PasswordResetToken> findByToken(String token);

    @Query("UPDATE PasswordResetToken c " +
            "SET c.confirmedAt = ?2 " +
            "WHERE c.token = ?1 ")
    @Modifying
    @Transactional
    void updateConfirmedAt(String passwordResetToken, LocalDateTime now);

}
