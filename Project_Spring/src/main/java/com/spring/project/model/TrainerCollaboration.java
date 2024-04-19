package com.spring.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

    private LocalDate endTime;

    CollaborationStatus  collaborationStatus;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    @NotNull
    private User trainer;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;
}
