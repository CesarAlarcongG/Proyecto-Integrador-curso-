package com.example.backendintegrador.exception;

public class AdministradorNotFoundException extends ResourceNotFoundException {
    public AdministradorNotFoundException(String message) {
        super(message);
    }
}
