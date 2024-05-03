package com.spring.project.token;


import com.spring.project.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Setter
@Table(name="_resetToken")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime confirmedAt;

    private LocalDateTime expiredAt;

    private boolean alreadyUsed;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "client_id"
    )
    private User user;

    public PasswordResetToken(String token, User user) {
        this.token = token;
        this.createdAt = LocalDateTime.now();
        this.confirmedAt = null;
        this.expiredAt = createdAt.plusMinutes(60);
        this.alreadyUsed = false;
        this.user = user;
    }
}
