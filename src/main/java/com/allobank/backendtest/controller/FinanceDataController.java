package com.allobank.backendtest.controller;

import com.allobank.backendtest.constant.ControllerConstants;
import com.allobank.backendtest.model.ResourceResult;
import com.allobank.backendtest.service.FinanceDataService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(ControllerConstants.FINANCE_BASE_URL)
public class FinanceDataController {
    private final FinanceDataService financeDataService;
    
    @GetMapping(ControllerConstants.DATA_ENDPOINT)
    public ResponseEntity<ResourceResult> getData(@PathVariable(ControllerConstants.RESOURCE_TYPE_VAR) @NotBlank String resourceType) {
        Object data = financeDataService.getData(resourceType);
        return ResponseEntity.ok(new ResourceResult(resourceType, data));
    }
}
