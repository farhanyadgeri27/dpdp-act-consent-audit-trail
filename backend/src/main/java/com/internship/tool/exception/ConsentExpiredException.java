package com.internship.tool.exception;

public class ConsentExpiredException extends RuntimeException {

    public ConsentExpiredException(Long id) {
        super("Consent record with id " + id + " has already expired.");
    }
}