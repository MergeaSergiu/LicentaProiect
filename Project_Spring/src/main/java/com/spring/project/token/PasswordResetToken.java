package com.spring.project.token;


import com.spring.project.model.Client;
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
    private Client client;

    public PasswordResetToken(String token, Client client) {
        this.token = token;
        this.createdAt = LocalDateTime.now();
        this.confirmedAt = null;
        this.expiredAt = createdAt.plusMinutes(20);
        this.alreadyUsed = false;
        this.client = client;
    }
}
