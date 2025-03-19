package com.taskmanagement.security.exceptions;

public class WrongPasswordException extends AuthorizationException {
    public WrongPasswordException() {
        super("Wrong password.");
    }
}