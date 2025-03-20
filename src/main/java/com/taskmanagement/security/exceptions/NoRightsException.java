package com.taskmanagement.security.exceptions;

public class NoRightsException extends AuthorizationException {
    public NoRightsException() {
        super("No rights.");
    }
}