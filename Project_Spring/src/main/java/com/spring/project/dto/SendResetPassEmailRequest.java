package com.spring.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@Setter
@Getter
public class SendResetPassEmailRequest {

    public SendResetPassEmailRequest(){
    }
    @JsonProperty("email")
    private String email;
}
