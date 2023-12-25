package com.spring.project.service;

import com.spring.project.model.EnrollmentTrainingClass;
import com.spring.project.repository.EnrollmentTrainingClassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentTrainingClassService {

    @Autowired
    private final EnrollmentTrainingClassRepository enrollmentTrainingClassRepository;

    public void saveEnrollmentAction(EnrollmentTrainingClass enrollmentTrainingClass){
        enrollmentTrainingClassRepository.save(enrollmentTrainingClass);
    }

    public List<EnrollmentTrainingClass> getClassesByUserId(Integer id) {
        return enrollmentTrainingClassRepository.findAllByUser_Id(id);
    }
}
