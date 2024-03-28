package com.spring.project.service.impl;

import com.spring.project.model.EnrollmentTrainingClass;
import com.spring.project.repository.EnrollmentTrainingClassRepository;
import com.spring.project.service.EnrollmentTrainingClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentTrainingClassServiceImpl implements EnrollmentTrainingClassService {

    @Autowired
    private final EnrollmentTrainingClassRepository enrollmentTrainingClassRepository;

    @Override
    public void saveEnrollmentAction(EnrollmentTrainingClass enrollmentTrainingClass){
        enrollmentTrainingClassRepository.save(enrollmentTrainingClass);
    }

    @Override
    public List<EnrollmentTrainingClass> getClassesByUserId(Long id) {
        return enrollmentTrainingClassRepository.findAllByuser_Id(id);
    }

    @Override
    public void deleteEnrollmentForUser(Long userId, Long trainingClassId) {
        enrollmentTrainingClassRepository.deleteByTrainingClassIdAndUserId(userId, trainingClassId);
    }

    @Override
    public void deleteAllEnrollsForTrainingClass(Long trainingClassId) {
        enrollmentTrainingClassRepository.deleteAllByTrainingClass_Id(trainingClassId);
    }

}
