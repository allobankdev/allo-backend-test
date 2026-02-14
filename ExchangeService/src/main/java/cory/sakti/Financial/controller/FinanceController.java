package cory.sakti.Financial.controller;

import cory.sakti.Financial.service.InMemoryDataStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/finance")
@RequiredArgsConstructor
public class FinanceController {
    private final InMemoryDataStoreService dataStore;


    @GetMapping("/{resource}")
    public ResponseEntity<Object> getFinancialData(@PathVariable String resource) {
        // RED PHASE: No lookup logic implemented yet.
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
