package com.spring.project.repository;

import com.spring.project.model.Court;
import com.spring.project.model.CourtDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourtDetailsRepository extends JpaRepository<CourtDetails, Long> {

   CourtDetails findByCourt(Court court);
}
