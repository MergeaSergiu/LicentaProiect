package com.spring.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "_insidefootball")
public class FotballInsideReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Integer id;

    private LocalDate localDate;

    @NotBlank(message = "An hour schedule must be selected for the reservation")
    private String hourSchedule;

    private String email;

    public FotballInsideReservation(){

    }

    public FotballInsideReservation(LocalDate localDate, String hourSchedule, String email) {
        this.localDate = localDate;
        this.hourSchedule = hourSchedule;
        this.email = email;
    }

}
