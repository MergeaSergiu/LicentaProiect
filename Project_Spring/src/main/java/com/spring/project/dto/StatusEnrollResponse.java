package com.spring.project.dto;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class StatusEnrollResponse {

    private String status;
}
