package com.example.bisection.dto;

public record BisectionResponse(
        double root,
        int iterations,
        String convergence
) {}