package com.chikohakles.allobank.agregator.controller;

import com.chikohakles.allobank.agregator.service.AgregatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/finance/data")
public class AgregatorController {
    private final AgregatorService agregatorService;

    @GetMapping("/{resourceType}")
    public ResponseEntity<?> getData(@PathVariable String resourceType) {
        return ResponseEntity.of(
                Optional.ofNullable(agregatorService.getData(resourceType))
        );
    }
}
