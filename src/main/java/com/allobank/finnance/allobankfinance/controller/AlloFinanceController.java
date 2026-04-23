package com.allobank.finnance.allobankfinance.controller;

import com.allobank.finnance.allobankfinance.dto.FinanceRequestDto;
import com.allobank.finnance.allobankfinance.service.strategy.FinanceDataStrategyResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/data")
@RequiredArgsConstructor
public class AlloFinanceController {


    private final FinanceDataStrategyResolver financeDataStrategyResolver;


    @GetMapping("/{resourceType}")
    public Object getFinanceData( @PathVariable String resourceType,
                                  @RequestParam(required = false) String startDate,
                                  @RequestParam(required = false) String endDate){
        log.debug("getFinanceData resourceType : {} ", resourceType);

        FinanceRequestDto requestDto = FinanceRequestDto.builder()
                .startDate(startDate)
                .endDate(endDate)
                .build();

        return financeDataStrategyResolver
                .resolve(resourceType)
                .fetchData(requestDto);
    }
}
