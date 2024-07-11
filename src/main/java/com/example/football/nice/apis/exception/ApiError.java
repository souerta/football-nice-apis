package com.example.football.nice.apis.exception;

public class ApiError {
    private String message;
    private String details;

    public ApiError(String message, String details) {
        this.message = message;
        this.details = details;
    }

    // Getters and setters
}
