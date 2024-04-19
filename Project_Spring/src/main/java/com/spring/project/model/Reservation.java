package com.spring.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank
    private String hourSchedule;

    Court court;

    @Temporal(TemporalType.DATE)
    @CreationTimestamp
    private LocalDate reservationMadeDate;

    @ManyToOne
    @JoinColumn(name= "user_id")
    @NotNull
    private User user;

}
