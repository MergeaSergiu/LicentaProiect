package com.spring.project.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Setter
@Table(name = "_trainerReservation")
@NoArgsConstructor
public class TrainerReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private LocalDate localDate;

    private String hourSchedule;

    private String firstName;

    private String lastName;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

}
