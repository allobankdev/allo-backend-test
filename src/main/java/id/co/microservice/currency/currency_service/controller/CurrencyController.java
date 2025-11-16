package id.co.microservice.currency.currency_service.controller;

import id.co.microservice.currency.currency_service.dto.CurrencyResponseDto;
import id.co.microservice.currency.currency_service.dto.FrankfurterResponseDto;
import id.co.microservice.currency.currency_service.exception.CurrencyException;
import id.co.microservice.currency.currency_service.service.CurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api/finance")
public class CurrencyController {

    public static final String LATEST_IDR_RATES = "latest_idr_usd";
    public static final String HISTORICAL_IDR_USD = "historical_idr_usd";
    public static final String SUPPORTED_CURRENCIES = "supported_currencies";

    private final CurrencyService currencyService;

    @Autowired
    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping(value = "/data/{resourceType}")
    public ResponseEntity<CurrencyResponseDto> getCurrency(@PathVariable("resourceType") String resourceType) {
        ResponseEntity<CurrencyResponseDto> response = null;

        switch (resourceType.toLowerCase()) {
            case LATEST_IDR_RATES:
                log.info("Fetching latest IDR to USD rates");
                CurrencyResponseDto latestRates = this.currencyService.getCurrencyLatestRates("IDR");
                response = new ResponseEntity<>(latestRates, HttpStatus.OK);
                return response;
            case HISTORICAL_IDR_USD:
                log.info("Fetching historical");
                CurrencyResponseDto historicalRates = this.currencyService.getCurrencyHistoricalRates();
                response = new ResponseEntity<>(historicalRates, HttpStatus.OK);
                return response;
            case SUPPORTED_CURRENCIES:
                log.info("Fetching currency list");
                CurrencyResponseDto currencies = this.currencyService.getSupportedCurrencies();
                response = new ResponseEntity<>(currencies, HttpStatus.OK);
                return response;
            default:
                log.warn("Unknown resource type requested: {}", resourceType);
                throw new CurrencyException("Unknown resource type: " + resourceType, HttpStatus.BAD_REQUEST);
        }
    }

}
