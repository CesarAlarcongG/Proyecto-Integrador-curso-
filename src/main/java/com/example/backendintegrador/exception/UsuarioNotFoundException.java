package com.example.backendintegrador.exception;

public class UsuarioNotFoundException extends ResourceNotFoundException {
    public UsuarioNotFoundException(String message) {
        super(message);
    }
}
