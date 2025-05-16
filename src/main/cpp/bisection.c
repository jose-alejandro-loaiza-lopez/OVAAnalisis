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
        printf("No se encontró el método evaluarFuncion\n");
        return NAN;
    }
    return (*env)->CallDoubleMethod(env, thisObj, mid, funcion, x);
}

JNIEXPORT jobject JNICALL Java_com_example_bisection_service_BisectionServiceNativo_encontrarRaiz
  (JNIEnv *env, jobject thisObj, jstring funcion, jdouble inferior, jdouble superior, jdouble tolerancia, jint maxIteraciones) {

    double a = inferior;
    double b = superior;
    double fa = evaluarFuncionDesdeJava(env, thisObj, funcion, a);
    double fb = evaluarFuncionDesdeJava(env, thisObj, funcion, b);

    if (multiplicar(fa, fb) >= 0) {
        printf("La función debe tener signos opuestos en los extremos.\n");
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

    // Obtener clase y constructor de BisectionResponse
    jclass clsResponse = (*env)->FindClass(env, "com/example/bisection/dto/BisectionResponse");
    if (clsResponse == NULL) return NULL;

    jmethodID constructor = (*env)->GetMethodID(env, clsResponse, "<init>", "(DILjava/lang/String;)V");
    if (constructor == NULL) return NULL;

    jstring convergenciaStr = (*env)->NewStringUTF(env, convergencia);
    return (*env)->NewObject(env, clsResponse, constructor, c, iteraciones + 1, convergenciaStr);
}
