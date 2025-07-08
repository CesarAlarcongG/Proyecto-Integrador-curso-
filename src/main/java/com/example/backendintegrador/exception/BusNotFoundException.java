package com.example.backendintegrador.exception;

public class BusNotFoundException extends ResourceNotFoundException {
    public BusNotFoundException(String message) {
        super(message);
    }
}
