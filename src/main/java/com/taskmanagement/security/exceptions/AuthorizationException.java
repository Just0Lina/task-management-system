package com.taskmanagement.security.exceptions;

import com.taskmanagement.exception.ValidationException;

public class AuthorizationException extends ValidationException {
    public AuthorizationException(String message) {
        super(message);
    }
}