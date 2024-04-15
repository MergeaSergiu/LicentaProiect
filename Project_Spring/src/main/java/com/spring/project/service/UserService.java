package com.spring.project.service;

import com.spring.project.dto.*;

import java.util.List;

public interface UserService {

     List<UserDataResponse> getAllClients();

     UserDataResponse getUserData(Long id);

     void deleteUser(Long id);

     void updateUserRole(Long id, RoleRequest roleRequest);

     List<TrainerResponse> getAllTrainers();

}
