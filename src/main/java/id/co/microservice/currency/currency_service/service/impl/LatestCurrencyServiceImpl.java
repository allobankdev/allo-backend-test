package id.co.microservice.currency.currency_service.service.impl;

import id.co.microservice.currency.currency_service.constant.CurrencyConstant;
import id.co.microservice.currency.currency_service.dto.CurrencyResponseDto;
import id.co.microservice.currency.currency_service.dto.FrankfurterResponseDto;
import id.co.microservice.currency.currency_service.feign.FrankfurterFeign;
import id.co.microservice.currency.currency_service.service.CurrencyStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component(CurrencyConstant.LATEST_IDR_RATES)
public class LatestCurrencyServiceImpl implements CurrencyStrategy {

    private final FrankfurterFeign frankfurterFeign;

    @Autowired
    public LatestCurrencyServiceImpl(FrankfurterFeign frankfurterFeign) {
        this.frankfurterFeign = frankfurterFeign;
    }

    @Override
    public CurrencyResponseDto execute() {
        log.info("Executing LatestCurrencyServiceImpl logic");
        FrankfurterResponseDto latestRate = this.frankfurterFeign.getLatestRate("IDR");

        CurrencyResponseDto currencyResponseDto = new CurrencyResponseDto();
        currencyResponseDto.setBase(latestRate.getBase());
        currencyResponseDto.setDate(latestRate.getDate());
        currencyResponseDto.setRates(latestRate.getRates());

        return currencyResponseDto;
    }

}
