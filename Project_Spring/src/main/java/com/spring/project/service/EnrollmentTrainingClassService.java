package com.spring.project.service;

import com.spring.project.model.EnrollmentTrainingClass;

import java.util.List;

public interface EnrollmentTrainingClassService {

    void saveEnrollmentAction(EnrollmentTrainingClass enrollmentTrainingClass);

    List<EnrollmentTrainingClass> getClassesByUserId(Long id);


    void deleteEnrollmentForUser(Long trainingClassId, Long clientId);

    void deleteAllEnrollsForTrainingClass(Long trainingClassId);
}
