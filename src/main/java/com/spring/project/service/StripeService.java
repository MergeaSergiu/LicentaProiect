package com.spring.project.service;

import com.spring.project.Exception.CustomExpiredJwtException;
import com.spring.project.dto.StripeChargeRequest;
import com.spring.project.dto.StripeTokenRequest;
import com.spring.project.model.Client;
import com.spring.project.model.Subscription;
import com.spring.project.model.SubscriptionPayment;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StripeService {

    @Autowired
    private final SubscriptionService subscriptionService;

    @Autowired
    private final ClientService clientService;

    @Autowired
    private final SubscriptionPaymentService subscriptionPaymentService;

    @Value("${stripe.api.publicKey}")
    private String stripeKey;

    public StripeTokenRequest createCardToken(StripeTokenRequest stripeTokenRequest){
        Stripe.apiKey = stripeKey;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()) {
            try {
                Map<String, Object> card = new HashMap<>();
                card.put("number", stripeTokenRequest.getCardNumber());
                card.put("exp_month", Integer.parseInt(stripeTokenRequest.getExpMonth()));
                card.put("exp_year", Integer.parseInt(stripeTokenRequest.getExpYear()));
                card.put("cvc", stripeTokenRequest.getCvc());
                card.put("username", authentication.getName());
                Map<String, Object> params = new HashMap<>();
                params.put("card", card);
                Token token = Token.create(params);
                if (token != null && token.getId() != null) {
                    stripeTokenRequest.setSuccess(true);
                    stripeTokenRequest.setToken(token.getId());
                    stripeTokenRequest.setUsername(authentication.getName());
                }
                return stripeTokenRequest;
            } catch (StripeException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        throw new CustomExpiredJwtException("Can not establish the conection");
    }

    public StripeChargeRequest charge(String className, StripeChargeRequest StripeChargeRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()) {
            Client client = clientService.findClientByEmail(authentication.getName());
            try {
                Subscription subscription = subscriptionService.findBySubscriptionName(className);
                if (subscription != null) {
                    Double amount = subscription.getSubscriptionPrice();
                    StripeChargeRequest.setSuccess(false);
                    Map<String, Object> chargeParams = new HashMap<>();
                    chargeParams.put("amount", (int) (amount * 100));
                    chargeParams.put("currency", "LEI");
                    chargeParams.put("description", "Payment for id " + StripeChargeRequest.getAdditionalInfo().getOrDefault("ID_TAG", ""));
                    chargeParams.put("source", StripeChargeRequest.getStripeToken());
                    Map<String, Object> metaData = new HashMap<>();
                    metaData.put("id", StripeChargeRequest.getChargedId());
                    metaData.putAll(StripeChargeRequest.getAdditionalInfo());
                    chargeParams.put("metadata", metaData);
                    Charge charge = Charge.create(chargeParams);
                    StripeChargeRequest.setMessage(charge.getOutcome().getSellerMessage());

                    if (charge.getPaid()) {
                        StripeChargeRequest.setChargedId(charge.getId());
                        StripeChargeRequest.setSuccess(true);
                        SubscriptionPayment subscriptionPayment = new SubscriptionPayment(
                                className,
                                amount,
                                true,
                                client
                        );
                        subscriptionPaymentService.createSubscriptionPayment(subscriptionPayment);
                        return StripeChargeRequest;
                    }
                }else{
                    throw new CustomExpiredJwtException("Subscription does not exist");
                }
            } catch (StripeException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        throw new CustomExpiredJwtException("Payment can not be performed.");
    }
}
