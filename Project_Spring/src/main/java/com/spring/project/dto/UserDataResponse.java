package com.spring.project.dto;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDataResponse {

    Integer id;
    String firstName;
    String lastName;
    String email;
    String clientRole;
}
