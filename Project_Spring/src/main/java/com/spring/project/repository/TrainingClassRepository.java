package com.spring.project.repository;

import com.spring.project.model.TrainingClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TrainingClassRepository extends JpaRepository<TrainingClass, Integer> {

    @Query("SELECT c FROM TrainingClass c where c.className = :className")
    TrainingClass getTrainingClassByName(@Param("className") String className);

    List<TrainingClass> findAllByTrainer_Id(Integer id);

    @Transactional
    @Modifying
    @Query("DELETE TrainingClass e WHERE e.className = :className")
    void deleteByClassName(@Param("className") String className);

}
