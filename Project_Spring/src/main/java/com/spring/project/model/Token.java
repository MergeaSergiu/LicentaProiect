package com.spring.project.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_tokenAccAndRef")
@Getter
@Setter
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Long id;
    public String token;
    @Enumerated(EnumType.STRING)
    public TokenType tokenType = TokenType.BEARER;

    @ManyToOne
    @JoinColumn(name = "client_id")
    public Client client;

}
