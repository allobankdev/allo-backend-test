package com.allo.finance.controller;

import com.allo.finance.client.FrankfurterClient;
import com.allo.finance.dto.FrankfurterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExchangeRateController {

    private final FrankfurterClient frankfurterClient;

    @GetMapping("/api/rates")
    public FrankfurterResponse getRate(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam Double amount
    ) {
        return frankfurterClient.getLatestRate(from, to, amount);
    }

}