package com.example.bisection.exception;

public class NonConvergenceException extends RuntimeException {
    public NonConvergenceException(String message) {
        super(message);
    }
}