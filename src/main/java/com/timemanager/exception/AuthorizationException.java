package com.timemanager.exception;

public class AuthorizationException extends RuntimeException {

    public AuthorizationException() {
        super("Authorization error: Incorrect password or username!");
    }
}
