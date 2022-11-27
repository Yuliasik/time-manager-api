package com.timemanager.exception;

public class UserNotExistException extends RuntimeException {

    public UserNotExistException(String username) {
        super(String.format("User with username=\"%s\" not exist!", username));
    }
}
