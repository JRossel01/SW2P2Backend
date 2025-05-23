package com.med.backend.exception;

public class DuplicateResourceException extends RuntimeException {
    private final String frontendMessage;

    public DuplicateResourceException(String resource, String field, String value, String frontendMessage) {
        super(resource + " with " + field + " '" + value + "' already exists.");
        this.frontendMessage = frontendMessage + ": '" + value + "'";
    }

    public String getFrontendMessage() {
        return frontendMessage;
    }
}
