    .text

    .global suma
suma:
    # double suma(double a, double b)
    # a en xmm0, b en xmm1
    addsd %xmm1, %xmm0
    ret

    .global dividir
dividir:
    # double dividir(double a, double b)
    # a en xmm0, b en xmm1
    divsd %xmm1, %xmm0
    ret

    .global resta
resta:
    # double resta(double a, double b)
    # a en xmm0, b en xmm1
    subsd %xmm1, %xmm0
    ret

    .global multiplicar
multiplicar:
    # double multiplicar(double a, double b)
    # a en xmm0, b en xmm1
    mulsd %xmm1, %xmm0
    ret
