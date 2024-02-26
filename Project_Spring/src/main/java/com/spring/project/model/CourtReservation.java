package com.spring.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "_reservation")
@NoArgsConstructor
public class CourtReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Integer id;

    private String localDate;

    private String hourSchedule;

    private String court;

    private String email;

    public CourtReservation(String localDate, String hourSchedule, String court, String email) {
        this.localDate = localDate;
        this.hourSchedule = hourSchedule;
        this.court = court;
        this.email = email;
    }
}
