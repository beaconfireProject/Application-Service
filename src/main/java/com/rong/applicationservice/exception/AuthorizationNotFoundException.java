package com.rong.applicationservice.exception;

public class AuthorizationNotFoundException extends RuntimeException {
    public AuthorizationNotFoundException(String message) {
        super(message);
    }
}
