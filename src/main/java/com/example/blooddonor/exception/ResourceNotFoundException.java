package com.example.blooddonor.exception;

// Thrown when someone asks for a donor/request that doesn't exist in the database.
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
