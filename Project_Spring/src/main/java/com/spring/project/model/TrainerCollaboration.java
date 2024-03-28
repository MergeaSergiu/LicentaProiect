package com.spring.project.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "_trainerCollaboration")
public class TrainerCollaboration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate startDate;

    private Boolean accepted;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private User trainer;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
