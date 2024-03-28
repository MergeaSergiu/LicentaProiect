package com.spring.project.controller;

import com.spring.project.service.TrainerCollaborationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping(path= "project/api/v1/collaboration/users")
public class TrainerCollaborationController {

    @Autowired
    private final TrainerCollaborationService trainerCollaborationService;
}
