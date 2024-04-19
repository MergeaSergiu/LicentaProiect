package com.spring.project.repository;

import com.spring.project.dto.ReservationRequest;
import com.spring.project.model.Court;
import com.spring.project.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {


    List<Reservation> findAllByOrderByReservationDateAsc();

    List<Reservation> findAllByUser_IdOrderByReservationDateAsc(@Param("id") Long id);

    @Query("SELECT e from Reservation e WHERE e.court = :court")
    List<Reservation> findByCourt(@Param("court") Court court);

    @Modifying
    @Transactional
    void deleteAllByUser_Id(@Param("id") Long id);

    List<Reservation> findAllByUser_IdAndReservationMadeDate(@Param("userId") Long userId, @Param("currentDate") LocalDate currentDate);
}
