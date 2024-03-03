package com.spring.project.dto;

import com.spring.project.model.Role;
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
    Role role;
}
