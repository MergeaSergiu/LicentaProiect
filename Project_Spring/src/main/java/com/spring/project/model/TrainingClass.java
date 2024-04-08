package com.spring.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "_trainingClass")
public class TrainingClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String className;

    private int duration;

    @NotBlank
    private String intensity;

    @NotBlank
    private String startTime;

    @NotBlank
    private String localDate;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    @NotNull
    private User trainer;

    public TrainingClass(String className, int duration, String startTime, String intensity, String localDate, User trainer) {
        this.className = className;
        this.duration = duration;
        this.intensity = intensity;
        this.localDate = localDate;
        this.startTime = startTime;
        this.trainer = trainer;
    }
}
