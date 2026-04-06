package com.finance.aggregator.controller;

import com.finance.aggregator.dto.UnifiedResponseDTO;
import com.finance.aggregator.service.FinanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/finance")
@RequiredArgsConstructor
public class FinanceController {

    private final FinanceService financeService;

    @GetMapping("/data/{resourceType}")
    public ResponseEntity<List<UnifiedResponseDTO>> getFinanceData(@PathVariable String resourceType) {
        Object data = financeService.getData(resourceType);
        return ResponseEntity.ok(List.of((UnifiedResponseDTO) data));
    }
}