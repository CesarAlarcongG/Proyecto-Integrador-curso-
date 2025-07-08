package com.example.backendintegrador.exception;

public class DuplicateCorreoException extends RuntimeException {
    public DuplicateCorreoException(String message) {
        super(message);
    }
}
