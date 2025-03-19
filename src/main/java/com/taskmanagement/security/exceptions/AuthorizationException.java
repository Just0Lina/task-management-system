package com.taskmanagement.security.exceptions;

import com.taskmanagement.exception.DomainException;

public class AuthorizationException extends DomainException {
    public AuthorizationException(String message) {
        super(message);
    }
}