package com.spring.project.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "_enrollmentUser")
public class EnrollmentTrainingClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Client user;

    @ManyToOne
    @JoinColumn(name = "training_class_id")
    private TrainingClass trainingClass;

}
