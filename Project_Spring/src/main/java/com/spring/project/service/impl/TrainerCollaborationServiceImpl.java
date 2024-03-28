package com.spring.project.service.impl;

import com.spring.project.service.TrainerService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TrainerCollaborationServiceImpl {

    @Autowired
    private final TrainerService trainerService;
}
