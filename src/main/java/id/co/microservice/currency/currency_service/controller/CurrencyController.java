package id.co.microservice.currency.currency_service.controller;

import id.co.microservice.currency.currency_service.dto.CurrencyResponseDto;
import id.co.microservice.currency.currency_service.service.CurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api/finance")
public class CurrencyController {

    private final CurrencyService currencyService;

    @Autowired
    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping(value = "/data/{resourceType}")
    public ResponseEntity<CurrencyResponseDto> getCurrency(@PathVariable("resourceType") String resourceType) {
        CurrencyResponseDto responseDto = this.currencyService.executeStrategy(resourceType);
        return ResponseEntity.ok(responseDto);
    }

}
