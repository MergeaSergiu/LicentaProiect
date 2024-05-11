package com.spring.project.mapper;

import com.spring.project.dto.PaymentRequest;
import com.spring.project.model.Subscription;
import com.spring.project.repository.SubscriptionRepository;
import com.spring.project.service.UserAccountService;
import com.spring.project.service.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@AllArgsConstructor
public class CardDataValidation {

    private final UserAccountService userAccountService;
    private final SubscriptionRepository subscriptionRepository;

    public boolean isValidCardHolderName(String cardHolderName) {
        String regex = "^[a-zA-Z]+(?:\\s[a-zA-Z]+)*$";
        return Pattern.matches(regex, cardHolderName);
    }
    
    public void cardValidation(String authorization, PaymentRequest paymentRequest){

        if (paymentRequest.getCardHolderName() == null || paymentRequest.getCardHolderName().trim().isEmpty()) {
            throw new IllegalArgumentException("No cardHolderName is used");
        }

        if (!isValidCardHolderName(paymentRequest.getCardHolderName())) {
            throw new IllegalArgumentException("Invalid cardHolderName format. It should contain only letters and optional whitespace between names.");
        }

        if(paymentRequest.getSubscriptionId() == null){
            throw new IllegalArgumentException("No subscription is related for this payment");
        }

        if(paymentRequest.getAmount() == null){
            throw new IllegalArgumentException("No amount is selected for this payment");
        }

        if(paymentRequest.getCurrency() == null){
            throw new IllegalArgumentException("No payment currency is selected");
        }

        if(!paymentRequest.getCurrency().equals("ron")){
            throw new IllegalArgumentException("Price must be set for 'RON' currency");
        }

        Subscription subscription = subscriptionRepository.findById(paymentRequest.getSubscriptionId()).orElseThrow(() -> new EntityNotFoundException("There is no subscription associated with this payment"));
        double subscriptionPrice = (double) paymentRequest.getAmount() /100;
        if(subscriptionPrice != subscription.getSubscriptionPrice()){
            throw new IllegalArgumentException("The price is not the same with the subscription price");
        }

        boolean hasActiveSubscription = userAccountService.getUserActiveSubscriptions(authorization);
        if (hasActiveSubscription) {
            throw new IllegalArgumentException("User has an active subscription. We can not proceed the payment");
        }
    }
    
}
