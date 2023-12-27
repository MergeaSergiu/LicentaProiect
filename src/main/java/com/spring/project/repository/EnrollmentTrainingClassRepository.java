package com.spring.project.repository;

import com.spring.project.model.EnrollmentTrainingClass;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface EnrollmentTrainingClassRepository extends JpaRepository<EnrollmentTrainingClass, Integer> {

    List<EnrollmentTrainingClass> findAllByUser_Id(Integer id);

    @Modifying
    @Transactional
    @Query("DELETE FROM EnrollmentTrainingClass  WHERE trainingClass.id = :trainingClassId AND user.id = :userId")
    void deleteByTrainingClassIdAndUserId(@Param("trainingClassId") Integer trainingClassId, @Param("userId") Integer userId);
}
