package id.co.microservice.currency.currency_service.service.impl;

import id.co.microservice.currency.currency_service.constant.CurrencyConstant;
import id.co.microservice.currency.currency_service.dto.CurrencyResponseDto;
import id.co.microservice.currency.currency_service.feign.FrankfurterFeign;
import id.co.microservice.currency.currency_service.service.CurrencyStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Slf4j
@Component(CurrencyConstant.SUPPORTED_CURRENCIES)
public class CurrenciesServiceImpl implements CurrencyStrategy {

    private final FrankfurterFeign frankfurterFeign;

    @Autowired
    public CurrenciesServiceImpl(FrankfurterFeign frankfurterFeign) {
        this.frankfurterFeign = frankfurterFeign;
    }

    @Override
    public CurrencyResponseDto execute() {
        log.info("Executing LatestCurrencyServiceImpl logic");
        HashMap<String, String> currencies = this.frankfurterFeign.getCurrencies();

        CurrencyResponseDto currencyResponseDto = new CurrencyResponseDto();
        currencyResponseDto.setCurrencies(currencies);

        return currencyResponseDto;
    }

}
