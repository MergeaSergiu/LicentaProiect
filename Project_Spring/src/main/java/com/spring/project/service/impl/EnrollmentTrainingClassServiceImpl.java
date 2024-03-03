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
    public List<EnrollmentTrainingClass> getClassesByUserId(Integer id) {
        return enrollmentTrainingClassRepository.findAllByUser_Id(id);
    }



    @Override
    public void deleteEnrollmentForUser(Integer userId, Integer trainingClassId) {
        enrollmentTrainingClassRepository.deleteByTrainingClassIdAndUserId(userId, trainingClassId);
    }

    @Override
    public void deleteAllEnrollsForTrainingClass(Integer trainingClassId) {
        enrollmentTrainingClassRepository.deleteAllByTrainingClass_Id(trainingClassId);
    }

}
