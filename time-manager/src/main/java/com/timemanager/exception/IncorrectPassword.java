package com.timemanager.exception;

public class IncorrectPassword extends RuntimeException {

    public IncorrectPassword() {
        super("Incorrect password!");
    }
}
