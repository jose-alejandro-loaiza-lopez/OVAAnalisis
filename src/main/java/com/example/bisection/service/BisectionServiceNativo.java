package com.example.bisection.service;

import com.example.bisection.dto.BisectionResponse;
import org.springframework.stereotype.Service;

@Service
public class BisectionServiceNativo {

    static {
        System.loadLibrary("bisection"); // Cargar la librería nativa llamada libbisection.so
    }

    // Declaramos el método nativo (la implementación estará en C)
    public native BisectionResponse encontrarRaiz(
            String funcion,
            double inferior,
            double superior,
            double tolerancia,
            int maxIteraciones
    );

    // Método para evaluar la función, que el código nativo llamará mediante JNI
    // Esto debe estar aquí para que C pueda invocar esta evaluación
    public double evaluarFuncion(String funcion, double x) {
        // Aquí puedes usar exp4j para evaluar la función
        try {
            var expresion = new net.objecthunter.exp4j.ExpressionBuilder(funcion)
                    .variable("x")
                    .build()
                    .setVariable("x", x);
            return expresion.evaluate();
        } catch (Exception e) {
            throw new RuntimeException("Error evaluando función en JNI: " + e.getMessage(), e);
        }
    }
}
