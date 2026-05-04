package com.internship.tool.exception;

import java.util.Map;

public class ValidationException extends RuntimeException {

    private final Map<String, String> errors;

    public ValidationException(Map<String, String> errors) {
        super("Validation failed: " + errors);
        this.errors = errors;
    }

    public Map<String, String> getErrors() { return errors; }
}