package com.sunrisedental.exception;

import java.util.List;

/**
 * Custom exception thrown when appointment validation fails.
 * 
 * Contains a list of all validation error messages so that
 * the caller can display them to the user.
 */
public class InvalidAppointmentException extends RuntimeException {

    private final List<String> validationErrors;

    public InvalidAppointmentException(List<String> validationErrors) {
        super("Appointment validation failed: " + String.join("; ", validationErrors));
        this.validationErrors = validationErrors;
    }

    public InvalidAppointmentException(String message) {
        super(message);
        this.validationErrors = List.of(message);
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }
}
