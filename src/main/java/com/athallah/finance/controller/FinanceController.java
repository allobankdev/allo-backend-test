package com.athallah.finance.controller;

import com.athallah.finance.service.FinanceService;
import com.athallah.finance.util.constant.ResourceType;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/finance")
public class FinanceController {

    private final FinanceService financeService;

    @GetMapping("/data/{resourceType}")
    public Object getData(@PathVariable ResourceType resourceType) {
        return financeService.getData(resourceType);
    }
}
