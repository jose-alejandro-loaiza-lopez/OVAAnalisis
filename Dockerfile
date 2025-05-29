FROM openjdk:23-jdk-slim

LABEL authors="josea"

# Instalar compilador y herramientas necesarias
RUN apt-get update && apt-get install -y build-essential

# Regresar al directorio raíz del proyecto
WORKDIR /app

COPY . .

# Compilar el archivo C
RUN gcc -fPIC -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/linux" \
    -c src/main/cpp/bisection.c -o bisection.o

# Compilar el archivo Assembly (sintaxis AT&T)
RUN gcc -fPIC -c src/main/cpp/aritmetica.s -o aritmetica.o

# Enlazar ambos objetos en una sola biblioteca compartida
RUN gcc -shared -z noexecstack -o libbisection.so bisection.o aritmetica.o

# Copiar la librería a /usr/lib para que pueda cargarse automáticamente
RUN mv libbisection.so /usr/lib/

ENTRYPOINT ["java", "-jar", "target/bisection-service-0.0.1-SNAPSHOT.jar"]