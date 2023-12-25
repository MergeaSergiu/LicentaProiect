package com.spring.project.repository;

import com.spring.project.model.EnrollmentTrainingClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentTrainingClassRepository extends JpaRepository<EnrollmentTrainingClass, Integer> {
List<EnrollmentTrainingClass> findAllByUser_Id(Integer id);
}
