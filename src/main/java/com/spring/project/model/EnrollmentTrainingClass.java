package com.spring.project.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "_enrollmentUser")
public class EnrollmentTrainingClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Client user;

    @ManyToOne
    @JoinColumn(name = "traing_class_id")
    private TrainingClass trainingClass;

    public EnrollmentTrainingClass(Client user, TrainingClass trainingClass){
        this.user = user;
        this.trainingClass = trainingClass;
    }

}
