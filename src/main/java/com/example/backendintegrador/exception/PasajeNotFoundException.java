package com.example.backendintegrador.exception;

public class PasajeNotFoundException extends ResourceNotFoundException {
    public PasajeNotFoundException(String message) {
        super(message);
    }
}
