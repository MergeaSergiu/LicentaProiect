package com.spring.project.token;


import com.spring.project.model.Client;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
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
        this.client = client;
    }
}
