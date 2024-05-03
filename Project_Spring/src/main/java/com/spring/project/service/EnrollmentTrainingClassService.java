package com.spring.project.service;

import com.spring.project.dto.TrainingClassResponse;

import java.util.List;

public interface EnrollmentTrainingClassService {

    void saveEnrollmentAction(Long trainingClassId, String authorization);

    List<TrainingClassResponse> getClassesForUser(String authorization);

    void deleteEnrollmentForUser(Long trainingClassId, String authorization);

}
