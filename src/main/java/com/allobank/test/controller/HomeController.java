package com.allobank.test.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Tag(name = "Home", description = "Basic health/info endpoint")
public class HomeController {

    @GetMapping("/")
    @Operation(summary = "Service info", description = "Returns basic app status and main endpoint info")
    public Map<String, Object> home() {
        return Map.of(
                "app", "allo-backend-test",
                "message", "Service is running",
                "endpoints", Map.of(
                        "finance_data", "/api/finance/data/{resourceType}"));
    }
}
