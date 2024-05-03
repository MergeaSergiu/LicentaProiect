package com.spring.project.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Setter
@Table(name = "courtDetails")
@NoArgsConstructor
public class CourtDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer startTime;

    private Integer endTime;

    Court court;
}
