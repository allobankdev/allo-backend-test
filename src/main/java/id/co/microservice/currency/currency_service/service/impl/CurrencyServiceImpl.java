package id.co.microservice.currency.currency_service.service.impl;

import id.co.microservice.currency.currency_service.dto.CurrencyResponseDto;
import id.co.microservice.currency.currency_service.dto.FrankfurterResponseDto;
import id.co.microservice.currency.currency_service.feign.FrankfurterFeign;
import id.co.microservice.currency.currency_service.service.CurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Slf4j
@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final FrankfurterFeign frankfurterFeign;

    @Autowired
    public CurrencyServiceImpl(FrankfurterFeign frankfurterFeign) {
        this.frankfurterFeign = frankfurterFeign;
    }

    @Override
    public CurrencyResponseDto getCurrencyLatestRates(String base) {
        log.info("Fetching latest currency rates for base: {}", base);
        FrankfurterResponseDto latestRate = this.frankfurterFeign.getLatestRate(base);

        CurrencyResponseDto currencyResponseDto = new CurrencyResponseDto();
        currencyResponseDto.setBase(latestRate.getBase());
        currencyResponseDto.setDate(latestRate.getDate());
        currencyResponseDto.setRates(latestRate.getRates());

        return currencyResponseDto;
    }

    @Override
    public CurrencyResponseDto getCurrencyHistoricalRates() {
        log.info("Fetching historical currency rates");
        FrankfurterResponseDto historicalRate = this.frankfurterFeign.getHistoricalRate();

        CurrencyResponseDto currencyResponseDto = new CurrencyResponseDto();
        currencyResponseDto.setBase(historicalRate.getBase());
        currencyResponseDto.setDate(historicalRate.getDate());
        currencyResponseDto.setRates(historicalRate.getRates());
        currencyResponseDto.setStartDate(historicalRate.getStartDate());
        currencyResponseDto.setEndDate(historicalRate.getEndDate());

        return currencyResponseDto;
    }

    @Override
    public CurrencyResponseDto getSupportedCurrencies() {
        log.info("Fetching supported currencies");
        HashMap<String, String> currencies = this.frankfurterFeign.getCurrencies();
        log.info("Supported currencies fetched: {}", currencies);

        CurrencyResponseDto currencyResponseDto = new CurrencyResponseDto();
        currencyResponseDto.setCurrencies(currencies);

        return currencyResponseDto;
    }
}
