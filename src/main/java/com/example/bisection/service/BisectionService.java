package com.example.bisection.service;

import com.example.bisection.dto.BisectionResponse;
import com.example.bisection.exception.*;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.springframework.stereotype.Service;

@Service
public class BisectionService {

    public BisectionResponse encontrarRaiz(String funcion, double inferior, double superior,
                                           Double tolerancia, Integer maxIteraciones) {
        validarEntradas(funcion, inferior, superior, tolerancia, maxIteraciones);

        double a = inferior;
        double b = superior;
        double fa = evaluarFuncion(funcion, a);
        double fb = evaluarFuncion(funcion, b);

        validarCondicionesIniciales(fa, fb);

        tolerancia = tolerancia != null ? tolerancia : 1e-7;
        int maxIter = maxIteraciones != null ? maxIteraciones : 100;

        int iteraciones = 0;
        double c = 0;
        double fc;
        String convergencia = "Máximo de iteraciones alcanzado";

        while (iteraciones < maxIter) {
            c = (a + b) / 2;
            fc = evaluarFuncion(funcion, c);

            if (Math.abs(fc) < tolerancia || (b - a)/2 < tolerancia) {
                convergencia = "Convergido dentro de la tolerancia";
                break;
            }

            if (fa * fc < 0) {
                b = c;
                fb = fc;
            } else {
                a = c;
                fa = fc;
            }
            iteraciones++;
        }

        if (iteraciones >= maxIter) {
            throw new NonConvergenceException("El método no convergió después de " + maxIter + " iteraciones");
        }

        return new BisectionResponse(c, iteraciones + 1, convergencia);
    }

    private void validarEntradas(String funcion, double inferior, double superior,
                                 Double tolerancia, Integer maxIteraciones) {
        if (inferior >= superior) {
            throw new InvalidIntervalException("El límite inferior debe ser menor que el superior");
        }

        try {
            new ExpressionBuilder(funcion).variable("x").build();
        } catch (IllegalArgumentException e) {
            throw new FunctionEvaluationException("Formato de función inválido: " + e.getMessage());
        }
    }

    private void validarCondicionesIniciales(double fa, double fb) {
        if (fa * fb >= 0) {
            throw new InvalidIntervalException("La función debe tener signos opuestos en los extremos del intervalo");
        }
    }

    private double evaluarFuncion(String funcion, double x) {
        try {
            Expression expresion = new ExpressionBuilder(funcion)
                    .variables("x")
                    .build()
                    .setVariable("x", x);
            return expresion.evaluate();
        } catch (ArithmeticException | IllegalArgumentException e) {
            throw new FunctionEvaluationException("Error evaluando la función en x = " + x + ": " + e.getMessage());
        }
    }
}