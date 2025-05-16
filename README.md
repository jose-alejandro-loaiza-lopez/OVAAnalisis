# API de Bisección

Este servicio proporciona un endpoint para calcular la raíz de una función utilizando el método de bisección.

## Endpoint

**POST** `/api/bisection`

### Descripción

Este endpoint recibe los parámetros necesarios para calcular la raíz de una función matemática en un intervalo, utilizando el método de bisección. La solicitud debe enviar un cuerpo en formato JSON que contenga la función, el límite inferior, el límite superior, la tolerancia y las iteraciones máximas.

### Campos de la solicitud (`BisectionRequest`)

#### `function` (String)
- **Descripción**: La función matemática que se va a evaluar. La variable debe ser `x` y la notación de la función debe usar `pow(x, n)` para exponentes.
- **Restricciones**: No puede estar vacía.
- **Ejemplo**: `"pow(x,3) - x - 2"`

#### `lower` (Double)
- **Descripción**: El límite inferior del intervalo en el que se buscará la raíz.
- **Restricciones**: Debe ser un valor numérico entre -1,000,000 y 1,000,000.
- **Ejemplo**: `1.0`

#### `upper` (Double)
- **Descripción**: El límite superior del intervalo en el que se buscará la raíz.
- **Restricciones**: Debe ser un valor numérico entre -1,000,000 y 1,000,000.
- **Ejemplo**: `2.0`

#### `tolerance` (Double)
- **Descripción**: La tolerancia para la convergencia del algoritmo de bisección.
- **Restricciones**: Debe ser un valor mayor o igual a `0.0000001`.
- **Ejemplo**: `0.0001`

#### `maxIterations` (Integer)
- **Descripción**: El número máximo de iteraciones permitidas para encontrar la raíz.
- **Restricciones**: Debe ser un valor entre `1` y `1000`.
- **Ejemplo**: `100`

---

## Ejemplo de solicitud método Java

### Solicitud `POST`

```bash
curl -X POST http://localhost:8080/api/bisection/java \
  -H "Content-Type: application/json" \
  -d '{
    "function": "pow(x,3) - x - 2",
    "lower": 1.0,
    "upper": 2.0,
    "tolerance": 0.0001,
    "maxIterations": 100
}'
```
```bash
curl -X POST http://localhost:8080/api/bisection/java -H "Content-Type: application/json" -d "{\"function\":\"pow(x,3) - x - 2\",\"lower\":1.0,\"upper\":2.0,\"tolerance\":0.0001,\"maxIterations\":100}"
```
## Ejemplo de solicitud método C (nativo)

### Solicitud `POST`

```bash
curl -X POST http://localhost:8080/api/bisection/native \
  -H "Content-Type: application/json" \
  -d '{
    "function": "pow(x,3) - x - 2",
    "lower": 1.0,
    "upper": 2.0,
    "tolerance": 0.0001,
    "maxIterations": 100
}'
```
```bash
curl -X POST http://localhost:8080/api/bisection/native -H "Content-Type: application/json" -d "{\"function\":\"pow(x,3) - x - 2\",\"lower\":1.0,\"upper\":2.0,\"tolerance\":0.0001,\"maxIterations\":100}"
```
