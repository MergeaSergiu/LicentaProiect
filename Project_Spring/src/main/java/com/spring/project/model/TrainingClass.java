package com.spring.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "_trainingClass")
public class TrainingClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String className;
    private int duration;
    private String intensity;
    private String startTime;
    private String localDate;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Client trainer;

    public TrainingClass(String className, int duration, String startTime, String intensity, String localDate, Client trainer) {
        this.className = className;
        this.duration = duration;
        this.intensity = intensity;
        this.localDate = localDate;
        this.startTime = startTime;
        this.trainer = trainer;
    }
}
