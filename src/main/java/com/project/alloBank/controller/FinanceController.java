package com.project.alloBank.controller;

import com.project.alloBank.service.DataStore;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/finance/data")
@RequiredArgsConstructor
public class FinanceController {

    private final DataStore dataStore;

    @GetMapping("/{resourceType}")
    public ResponseEntity<?> getData(@PathVariable String resourceType) {
        Object data = dataStore.get(resourceType);
        if (data == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(data);
    }

}
