package com.spring.project.dto;


import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RoleRequest {

    @NotNull
    private Integer id;
}
