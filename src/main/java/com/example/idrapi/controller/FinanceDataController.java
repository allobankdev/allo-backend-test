package com.example.idrapi.controller;

import com.example.idrapi.model.FinanceDataResponse;
import com.example.idrapi.service.FinanceDataService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/finance/data")
@Slf4j
public class FinanceDataController {
    private final FinanceDataService financeDataService;

    public FinanceDataController(FinanceDataService financeDataService) {
        this.financeDataService = financeDataService;
    }

    @GetMapping("/{resourceType}")
    public ResponseEntity<FinanceDataResponse> getFinanceData(
            @PathVariable String resourceType) {

        log.debug("ResourceType: '{}'", resourceType);

        FinanceDataResponse response = financeDataService.getData(resourceType)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(
                                "Resource type '%s' not found. Valid types: %s",
                                resourceType,
                                financeDataService.getRegisteredResourceTypes()
                        )
                ));
        log.info("response data class {} {}", financeDataService.getClass(), response);

        return ResponseEntity.ok(response);
    }
}
