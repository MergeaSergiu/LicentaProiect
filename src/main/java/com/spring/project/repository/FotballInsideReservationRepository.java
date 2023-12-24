package com.spring.project.repository;

import com.spring.project.model.FotballInsideReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FotballInsideReservationRepository extends JpaRepository<FotballInsideReservation, Integer> {

    @Query("SELECT e FROM FotballInsideReservation e WHERE function('date', e.localDate) = :currentDate")
    List<FotballInsideReservation> findByLocalDateCurrentDate(@Param("currentDate") LocalDate currentDate);

    List<FotballInsideReservation> findAll();

    @Query("SELECT e from FotballInsideReservation e WHERE e.email = :email")
    List<FotballInsideReservation> findReservationsByUser(@Param("email") String email);

    @Transactional
    @Modifying
    @Query("DELETE from FotballInsideReservation e WHERE e.email = :email AND e.hourSchedule = :hourSchedule AND e.localDate = :localDate")
    void deleteReservation(@Param("email") String email, @Param("hourSchedule") String hourSchedule, @Param("localDate") LocalDate localDate);
}
