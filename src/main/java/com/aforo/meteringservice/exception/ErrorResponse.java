package com.aforo.meteringservice.exception;

import lombok.Data;

@Data
public class ErrorResponse {
    private String errorCode;
    private String errorMessage;

    public ErrorResponse(String calculationError, String message) {
        this.errorCode = calculationError;
        this.errorMessage = message;
    }
}

