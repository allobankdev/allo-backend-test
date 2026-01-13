package com.example.assesment_be_allo.controller;
import com.example.assesment_be_allo.service.FinanceDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/finance")
public class FinanceController {

    private final FinanceDataService financeDataService;

    public FinanceController(FinanceDataService financeDataService) {
        this.financeDataService = financeDataService;
    }

    @GetMapping("/data/{resourceType}")
    public ResponseEntity<?> getFinanceData(@PathVariable String resourceType) {
        Object result = financeDataService.fetchData(resourceType);
        return ResponseEntity.ok(result);
    }
}
