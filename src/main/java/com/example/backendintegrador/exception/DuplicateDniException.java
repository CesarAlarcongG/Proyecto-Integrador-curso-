package com.example.backendintegrador.exception;

public class DuplicateDniException extends RuntimeException {
    public DuplicateDniException(String message) {
        super(message);
    }
}
