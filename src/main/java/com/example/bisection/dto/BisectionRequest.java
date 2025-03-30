package com.example.bisection.dto;

import jakarta.validation.constraints.*;

public record BisectionRequest(
        @NotBlank(message = "La función no puede estar vacía")
        String function,

        @NotNull(message = "Límite inferior no puede ser nulo")
        @DecimalMin(value = "-1000000", message = "Límite inferior debe ser ≥ -1.000.000")
        @DecimalMax(value = "1000000", message = "Límite inferior debe ser ≤ 1.000.000")
        Double lower,

        @NotNull(message = "Límite superior no puede ser nulo")
        @DecimalMin(value = "-1000000", message = "Límite superior debe ser ≥ -1.000.000")
        @DecimalMax(value = "1000000", message = "Límite superior debe ser ≤ 1.000.000")
        Double upper,

        @DecimalMin(value = "0.0000001", message = "La tolerancia debe ser ≥ 0.0000001")
        Double tolerance,

        @Min(value = 1, message = "Iteraciones máximas debe ser ≥ 1")
        @Max(value = 1000, message = "Iteraciones máximas debe ser ≤ 1000")
        Integer maxIterations
) {}