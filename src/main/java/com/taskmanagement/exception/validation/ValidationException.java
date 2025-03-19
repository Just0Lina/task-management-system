package com.taskmanagement.exception.validation;

import com.taskmanagement.exception.DomainException;

public class ValidationException extends DomainException {
    public ValidationException(String message) {
        super(message);
    }
}