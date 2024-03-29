package com.spring.project.repository;

import com.spring.project.model.TrainerCollaboration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainerCollaborationRepository extends JpaRepository<TrainerCollaboration, Long> {

    List<TrainerCollaboration> findAllByTrainer_Id(@Param("id") Long id);
}
