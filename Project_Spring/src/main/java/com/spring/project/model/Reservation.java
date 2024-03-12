package com.spring.project.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Setter
@Table(name = "_reservation")
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Integer id;

    private String localDate;

    private String hourSchedule;

    private String court;

    @ManyToOne
    @JoinColumn(name= "user_id")
    private Client user;

    public Reservation(String localDate, String hourSchedule, String court, Client user) {
        this.localDate = localDate;
        this.hourSchedule = hourSchedule;
        this.court = court;
        this.user = user;
    }
}
