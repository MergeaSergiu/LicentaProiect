package com.spring.project.repository;

import com.spring.project.model.CourtReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<CourtReservation, Integer> {

    @Query("SELECT e FROM CourtReservation e WHERE function('date', e.localDate) = :currentDate")
    List<CourtReservation> findByLocalDateCurrentDate(@Param("currentDate") LocalDate currentDate);

    List<CourtReservation> findAll();

    @Query("SELECT e from CourtReservation e WHERE e.email = :email")
    List<CourtReservation> findReservationsByUser(@Param("email") String email);

    @Transactional
    @Modifying
    @Query("DELETE from CourtReservation e WHERE e.email = :email AND e.hourSchedule = :hourSchedule AND e.localDate = :localDate AND e.court = :court")
    void deleteReservation(@Param("email") String email, @Param("hourSchedule") String hourSchedule, @Param("localDate") LocalDate localDate, @Param("court") String court);

    @Query("SELECT e from CourtReservation e WHERE e.court = :court")
    List<CourtReservation> findByCourt(@Param("court") String court);
}
