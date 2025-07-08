package com.example.backendintegrador.exception;

public class ConductorNotFoundException extends ResourceNotFoundException {
    public ConductorNotFoundException(String message) {
        super(message);
    }
}
