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
@Component(CurrencyConstant.HISTORICAL_IDR_USD)
public class HistoricalServiceImpl implements CurrencyStrategy {

    private final FrankfurterFeign frankfurterFeign;

    @Autowired
    public HistoricalServiceImpl(FrankfurterFeign frankfurterFeign) {
        this.frankfurterFeign = frankfurterFeign;
    }

    @Override
    public CurrencyResponseDto execute() {
        log.info("Executing LatestCurrencyServiceImpl logic");
        FrankfurterResponseDto historicalRate = this.frankfurterFeign.getHistoricalRate();

        CurrencyResponseDto currencyResponseDto = new CurrencyResponseDto();
        currencyResponseDto.setBase(historicalRate.getBase());
        currencyResponseDto.setDate(historicalRate.getDate());
        currencyResponseDto.setRates(historicalRate.getRates());
        currencyResponseDto.setStartDate(historicalRate.getStartDate());
        currencyResponseDto.setEndDate(historicalRate.getEndDate());

        return currencyResponseDto;
    }

}
