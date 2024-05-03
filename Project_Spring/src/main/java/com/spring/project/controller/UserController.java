package com.spring.project.controller;

import com.spring.project.dto.*;
import com.spring.project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "project/api/v1/users")
public class UserController {

    @Autowired
    private final UserService userService;


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserDataResponse>> getAllUsers(){
        List<UserDataResponse> userDataResponse = userService.getAllClients();
        return new ResponseEntity<>(userDataResponse, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDataResponse> getUserData(@PathVariable("userId") Long userId){
        UserDataResponse userDataResponse = userService.getUserData(userId);
        return new ResponseEntity<>(userDataResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") Long userId){
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{userId}/role")
    public ResponseEntity<Void> updateUserRole(@PathVariable("userId") Long userId, @RequestBody RoleRequest roleRequest){
        userService.updateUserRole(userId, roleRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/trainers")
    public ResponseEntity<List<TrainerResponse>> getAllTrainers(){
        List<TrainerResponse> trainersResponses = userService.getAllTrainers();
        return new ResponseEntity<>(trainersResponses, HttpStatus.OK);
    }

}
