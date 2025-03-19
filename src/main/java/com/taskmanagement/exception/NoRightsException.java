package com.taskmanagement.exception;

import com.taskmanagement.security.exceptions.AuthorizationException;

public class NoRightsException extends AuthorizationException {
    public NoRightsException() {
        super("No rights.");
    }
}