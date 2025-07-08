package com.example.backendintegrador.exception;

public class AgenciaNotFoundException extends ResourceNotFoundException {
    public AgenciaNotFoundException(String message) {
        super(message);
    }
}
