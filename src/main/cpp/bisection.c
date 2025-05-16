#include "com_example_bisection_service_BisectionServiceNativo.h"
#include <stdio.h>
#include <math.h>

extern double suma(double a, double b);
extern double dividir(double a, double b);
extern double resta(double a, double b);
extern double multiplicar(double a, double b);

// Función auxiliar para llamar a evaluarFuncion desde Java
double evaluarFuncionDesdeJava(JNIEnv *env, jobject thisObj, jstring funcion, double x) {
    jclass cls = (*env)->GetObjectClass(env, thisObj);
    jmethodID mid = (*env)->GetMethodID(env, cls, "evaluarFuncion", "(Ljava/lang/String;D)D");
    if (mid == NULL) {
        jclass exc = (*env)->FindClass(env, "com/example/bisection/exception/FunctionEvaluationException");
        if (exc != NULL) {
            (*env)->ThrowNew(env, exc, "No se encontró el método evaluarFuncion en el objeto Java.");
        }
        return NAN;
    }
    return (*env)->CallDoubleMethod(env, thisObj, mid, funcion, x);
}

JNIEXPORT jobject JNICALL Java_com_example_bisection_service_BisectionServiceNativo_encontrarRaiz
  (JNIEnv *env, jobject thisObj, jstring funcion, jdouble inferior, jdouble superior, jdouble tolerancia, jint maxIteraciones) {

    if (inferior >= superior) {
        jclass exc = (*env)->FindClass(env, "com/example/bisection/exception/InvalidIntervalException");
        if (exc != NULL) {
            (*env)->ThrowNew(env, exc, "El límite inferior debe ser menor que el superior.");
        }
        return NULL;
    }

    double a = inferior;
    double b = superior;
    double fa = evaluarFuncionDesdeJava(env, thisObj, funcion, a);
    double fb = evaluarFuncionDesdeJava(env, thisObj, funcion, b);

    if (multiplicar(fa, fb) >= 0) {
        jclass exc = (*env)->FindClass(env, "com/example/bisection/exception/InvalidIntervalException");
        if (exc != NULL) {
            (*env)->ThrowNew(env, exc, "La función debe tener signos opuestos en los extremos.");
        }
        return NULL;
    }

    double c = 0.0, fc;
    int iteraciones = 0;
    const char *convergencia = "Máximo de iteraciones alcanzado";

    while (iteraciones < maxIteraciones) {
        c = dividir(suma(a, b), 2.0);
        fc = evaluarFuncionDesdeJava(env, thisObj, funcion, c);

        if (fabs(fc) < tolerancia || dividir(resta(b, a), 2.0) < tolerancia) {
            convergencia = "Convergido dentro de la tolerancia";
            break;
        }

        if (multiplicar(fa, fc) < 0) {
            b = c;
            fb = fc;
        } else {
            a = c;
            fa = fc;
        }
        iteraciones++;
    }

    if (iteraciones >= maxIteraciones) {
        jclass exc = (*env)->FindClass(env, "com/example/bisection/exception/NonConvergenceException");
        if (exc != NULL) {
            (*env)->ThrowNew(env, exc, "El método no convergió después del número máximo de iteraciones.");
        }
        return NULL;
    }

    // Crear el objeto BisectionResponse
    jclass clsResponse = (*env)->FindClass(env, "com/example/bisection/dto/BisectionResponse");
    if (clsResponse == NULL) return NULL;

    jmethodID constructor = (*env)->GetMethodID(env, clsResponse, "<init>", "(DILjava/lang/String;)V");
    if (constructor == NULL) return NULL;

    jstring convergenciaStr = (*env)->NewStringUTF(env, convergencia);
    return (*env)->NewObject(env, clsResponse, constructor, c, iteraciones + 1, convergenciaStr);
}
