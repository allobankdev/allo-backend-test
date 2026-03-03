package com.ade.exchangerateagregator.adapter.in.rest;

import com.ade.exchangerateagregator.application.dto.FinanceBaseResponse;
import com.ade.exchangerateagregator.application.service.FinanceService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/finance")
@AllArgsConstructor
public class ExchangeRateController {
    private final FinanceService financeService;

    @GetMapping("/data/{resourceType}")
    public List<? extends FinanceBaseResponse> getFinanceData(@PathVariable String resourceType){
        return financeService.getFinanceData(resourceType);
    }
}
