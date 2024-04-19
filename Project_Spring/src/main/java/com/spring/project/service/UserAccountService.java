package com.spring.project.service;
import com.spring.project.dto.*;
import com.spring.project.model.SubscriptionsHistory;


import java.util.List;

public interface UserAccountService {

    void updateUserProfile(UpdateUserRequest updateUserRequest,String authorization);

    UserDataResponse getUserProfileData(String authorization);

    boolean getUserActiveSubscriptions(String authorization);
}
