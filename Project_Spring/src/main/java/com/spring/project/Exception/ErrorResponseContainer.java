package com.spring.project.Exception;

import lombok.Data;

@Data
public class ErrorResponseContainer {
        private String errorMessage;
        private Integer httpStatusCode;
}
