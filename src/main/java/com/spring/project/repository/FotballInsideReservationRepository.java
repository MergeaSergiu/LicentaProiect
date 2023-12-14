package com.spring.project.repository;

import com.spring.project.model.FotballInsideReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface FotballInsideReservationRepository extends JpaRepository<FotballInsideReservation, Integer> {

    @Query("SELECT e FROM FotballInsideReservation e WHERE function('date', e.localDate) = :currentDate")
    List<FotballInsideReservation> findByLocalDateCurrentDate(@Param("currentDate") LocalDate currentDate);

    List<FotballInsideReservation> findAll();

    @Query("SELECT e from FotballInsideReservation e WHERE e.email = :email")
    List<FotballInsideReservation> findReservationsByUser(@Param("email") String email);

}
