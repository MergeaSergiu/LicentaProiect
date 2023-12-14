package com.spring.project.dto;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientResponse {

    String firstName;
    String lastName;
    String email;
}
