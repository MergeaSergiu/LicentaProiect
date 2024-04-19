package com.spring.project.repository;

import com.spring.project.model.EnrollmentTrainingClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface EnrollmentTrainingClassRepository extends JpaRepository<EnrollmentTrainingClass, Long> {

    List<EnrollmentTrainingClass> findAllByuser_Id(Long id);

    @Modifying
    @Transactional
    @Query("DELETE FROM EnrollmentTrainingClass WHERE trainingClass.id = :trainingClassId AND user.id = :userId")
    void deleteByTrainingClassIdAndUserId(@Param("trainingClassId") Long trainingClassId, @Param("userId") Long userId);

    @Modifying
    @Transactional
    void deleteAllByTrainingClass_Id(@Param("trainingClassId") Long trainingClassId);

    @Modifying
    @Transactional
    void deleteAllByUser_id(@Param("user_id") Long userId);

    EnrollmentTrainingClass findByTrainingClass_IdAndUser_Id(@Param("trainingClass_id") Long trainingClassId, @Param("user_id") Long userId);

}
