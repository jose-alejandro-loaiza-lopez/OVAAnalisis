package com.example.bisection.controller;

import com.example.bisection.dto.BisectionRequest;
import com.example.bisection.dto.BisectionResponse;
import com.example.bisection.service.BisectionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bisection")
public class BisectionController {

    private final BisectionService bisectionService;

    public BisectionController(BisectionService bisectionService) {
        this.bisectionService = bisectionService;
    }

    @PostMapping
    public ResponseEntity<BisectionResponse> calcularRaiz(@Valid @RequestBody BisectionRequest request) {
        BisectionResponse response = bisectionService.encontrarRaiz(
                request.function(),
                request.lower(),
                request.upper(),
                request.tolerance(),
                request.maxIterations()
        );
        return ResponseEntity.ok(response);
    }
}