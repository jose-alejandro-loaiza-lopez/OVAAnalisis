package com.example.bisection.controller;

import com.example.bisection.dto.BisectionRequest;
import com.example.bisection.dto.BisectionResponse;
import com.example.bisection.service.BisectionService;
import com.example.bisection.service.BisectionServiceNativo;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bisection")
public class BisectionController {

    private final BisectionService javaService;
    private final BisectionServiceNativo nativeService;

    public BisectionController(BisectionService javaService, BisectionServiceNativo nativeService) {
        this.javaService = javaService;
        this.nativeService = nativeService;
    }

    // Endpoint con implementación en Java
    @PostMapping("/java")
    public ResponseEntity<BisectionResponse> calcularRaizJava(@Valid @RequestBody BisectionRequest request) {
        BisectionResponse response = javaService.encontrarRaiz(
                request.function(),
                request.lower(),
                request.upper(),
                request.tolerance(),
                request.maxIterations()
        );
        return ResponseEntity.ok(response);
    }

    // Endpoint con implementación en C/Assembly a través de JNI
    @PostMapping("/native")
    public ResponseEntity<BisectionResponse> calcularRaizNativo(@Valid @RequestBody BisectionRequest request) {
        BisectionResponse response = nativeService.encontrarRaiz(
                request.function(),
                request.lower(),
                request.upper(),
                request.tolerance(),
                request.maxIterations()
        );
        return ResponseEntity.ok(response);
    }
}
