package com.spring.project.model;

import jakarta.persistence.*;
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
    private String className;
    private int duration;
    private String intensity;
    private String startTime;
    private String localDate;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
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
