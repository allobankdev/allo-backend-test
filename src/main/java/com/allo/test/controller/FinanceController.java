package com.allo.test.controller;

import com.allo.test.service.ExternalApiService;
import com.allo.test.service.FinanceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceController {

    private final FinanceService financeService;
    private final ExternalApiService externalApiService;

    public FinanceController(FinanceService financeService,
                             ExternalApiService externalApiService) {
        this.financeService = financeService;
        this.externalApiService = externalApiService;
    }

    @GetMapping("/resourceType")
    public Object getData(
            @PathVariable String resourceType){
        return financeService.getData(resourceType);
    }

    @GetMapping("/test")
    public String testApi() {
        return externalApiService.testCall();
    }
}
