package com.spring.project.repository;

import com.spring.project.model.CollaborationStatus;
import com.spring.project.model.TrainerCollaboration;
import com.spring.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TrainerCollaborationRepository extends JpaRepository<TrainerCollaboration, Long> {

    List<TrainerCollaboration> findAllByTrainer_Id(@Param("id") Long id);

    List<TrainerCollaboration> findAllByUser_Id(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE TrainerCollaboration s SET s.collaborationStatus = :collaborationStatus WHERE s.id = :collaborationId")
    void updateCollaborationStatus(@Param("collaborationId") Long collaborationId, @Param("collaborationStatus") CollaborationStatus collaborationStatus);

    @Query("SELECT tc FROM TrainerCollaboration tc " +
            "WHERE tc.user = :user " +
            "AND tc.collaborationStatus IN (:statuses)")
    List<TrainerCollaboration> findByUserAndCollaborationStatusIn(
            @Param("user") User user,
            @Param("statuses") List<CollaborationStatus> statuses
    );
}
