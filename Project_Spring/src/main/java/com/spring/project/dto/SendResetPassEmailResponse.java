package com.spring.project.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@Setter
@Getter
public class SendResetPassEmailResponse {

    private String token;
}
