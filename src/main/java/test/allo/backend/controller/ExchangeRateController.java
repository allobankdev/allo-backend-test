package test.allo.backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import test.allo.backend.service.IDRDataFetcher;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/finance/data")
public class ExchangeRateController {

    private final Map<String, IDRDataFetcher> service;

    @GetMapping("/{resourceType}")
    public ResponseEntity<JsonNode> getResource(@PathVariable String resourceType) {
        return ResponseEntity.ok(service.get(resourceType).fetchData());
    }
}
