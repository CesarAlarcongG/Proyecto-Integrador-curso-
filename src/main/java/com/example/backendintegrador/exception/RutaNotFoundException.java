package com.example.backendintegrador.exception;

public class RutaNotFoundException extends ResourceNotFoundException {
    public RutaNotFoundException(String message) {
        super(message);
    }
}