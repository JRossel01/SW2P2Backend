package com.med.backend.exception;

import java.util.List;

public class MethodArgumentNotValidException extends RuntimeException {
    private final List<String> errorMessages;

    public MethodArgumentNotValidException(String message, List<String> errorMessages) {
        super(message);
        this.errorMessages = errorMessages;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }
}
