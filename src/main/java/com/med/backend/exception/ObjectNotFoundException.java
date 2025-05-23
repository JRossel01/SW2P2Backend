package com.med.backend.exception;

public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(String resource, String field, String value) {
        super(resource + " with " + field + " '" + value + "' was not found.");
    }

    public ObjectNotFoundException(String message) {
        super(message);
    }
}
