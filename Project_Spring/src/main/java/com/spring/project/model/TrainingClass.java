package com.spring.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "^[a-zA-Z]+(\\s[a-zA-Z]+)*$", message = "Class name must contain only letters")
    private String className;

    @NotNull(message = "Duration Time should not be empty")
    private Integer duration;

    @NotBlank(message = "Intensity should not be empty")
    private String intensity;

    @NotBlank(message = "StartTime should not be empty")
    @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Start time should be in the format hh:mm")
    private String startTime;

    @NotBlank(message = "localDate should not be empty")
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
