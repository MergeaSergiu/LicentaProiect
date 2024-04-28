package com.spring.project.service.impl;

import com.spring.project.dto.*;
import com.spring.project.mapper.*;
import com.spring.project.model.*;
import com.spring.project.repository.*;
import com.spring.project.service.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final SubscriptionHistoryRepository subscriptionHistoryRepository;
    private final ReservationService reservationService;
    private final TrainingClassService trainingClassService;
    private final ClientRepository clientRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final PasswordResetTokenService passwordResetTokenService;
    private final RoleRepository roleRepository;
    private final UserDataMapper userDataMapper;
    private final TrainerDataMapper trainerDataMapper;
    private final EnrollmentTrainingClassRepository enrollmentTrainingClassRepository;
    private final TrainerCollaborationRepository trainerCollaborationRepository;
    private final TrainingClassRepository trainingClassRepository;

    @Override
    public List<UserDataResponse> getAllClients() {
            List<User> users = clientRepository.findAll();
              return users.stream()
                    .map(userDataMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public UserDataResponse getUserData(Long id) {
        User user = clientRepository.findById(id).orElse(null);
        if(user == null){
            throw new EntityNotFoundException("User does not exist");
        }
        return userDataMapper.convertToDto(user);
    }

    @Override
    public void deleteUser(Long id) {
            User user = clientRepository.findById(id).orElse(null);
            if(user == null){
                throw new EntityNotFoundException("User does not exist");
            }
            if(user.getRole().getName().equals("TRAINER")){
                List<TrainingClass> trainingClasses = trainingClassService.getTrainingClassesForTrainer(id);
                if(trainingClasses != null){
                    for(TrainingClass trainingClass : trainingClasses){
                            trainingClassRepository.deleteById(trainingClass.getId());
                            enrollmentTrainingClassRepository.deleteAllByTrainingClass_Id(id);
                            trainerCollaborationRepository.deleteAllByTrainer_Id(id);
                    }
                }
            }else if(user.getRole().getName().equals("USER")) {
                reservationService.deleteReservationsForUser(id);
                enrollmentTrainingClassRepository.deleteAllByUser_id(id);
                subscriptionHistoryRepository.deleteAllByUser_Id(id);
                trainerCollaborationRepository.deleteAllByUser_Id(id);
            }
        confirmationTokenService.deleteByclient_Id(id);
        passwordResetTokenService.deleteByclient_Id(id);
        clientRepository.deleteById(id);
    }

    @Override
    public void updateUserRole(Long id, RoleRequest roleRequest) {
            User user = clientRepository.findById(id).orElse(null);
            if(user == null){
            throw new EntityNotFoundException("User does not exist");
            }
            Role role = roleRepository.findById(Long.valueOf(roleRequest.getId())).orElse(null);
            if(role == null){
                throw new EntityNotFoundException("Role does not exist");
            }

            if(user.getRole().getName().equals("USER") && !role.getName().equals("USER")){
                reservationService.deleteReservationsForUser(user.getId());
                enrollmentTrainingClassRepository.deleteAllByUser_id(user.getId());
                subscriptionHistoryRepository.deleteAllByUser_Id(id);
                trainerCollaborationRepository.deleteAllByUser_Id(id);
            }else if(user.getRole().getName().equals("TRAINER") && !role.getName().equals("TRAINER")){
                List<TrainingClass> trainingClasses = trainingClassService.getTrainingClassesForTrainer(id);
                if(trainingClasses != null){
                    for(TrainingClass trainingClass : trainingClasses){
                        enrollmentTrainingClassRepository.deleteAllByTrainingClass_Id(trainingClass.getId());
                        trainingClassRepository.deleteById(trainingClass.getId());
                    }
                }
                trainerCollaborationRepository.deleteAllByTrainer_Id(id);
            }
            user.setRole(role);
            clientRepository.save(user);
    }

    @Override
    public List<TrainerResponse> getAllTrainers() {
        List<User> trainers = clientRepository.getAllTrainers();
        return trainers.stream()
                .map(trainerDataMapper::convertToDto).collect(Collectors.toList());
    }


}