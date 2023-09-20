package com.invex.Exceptions;

public class ExistingUserException extends RuntimeException{
    public ExistingUserException(String message) {
        super(message);
    }
}
