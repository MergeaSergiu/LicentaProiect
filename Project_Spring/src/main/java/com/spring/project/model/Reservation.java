package com.spring.project.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Setter
@Table(name = "_reservation")
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private LocalDate reservationDate;

    private String hourSchedule;

    private String court;

    @Temporal(TemporalType.DATE)
    @CreationTimestamp
    private LocalDate reservationMadeDate;

    @ManyToOne
    @JoinColumn(name= "user_id")
    private User user;

}
