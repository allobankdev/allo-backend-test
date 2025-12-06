package com.bank.allo.rest.mapper;

import com.bank.allo.domain.idr.HistoricalRates;
import com.bank.allo.domain.idr.LatestRates;
import com.bank.allo.domain.idr.SupportedCurrencies;
import com.bank.allo.rest.entity.historical.HistoricalRatesResponse;
import com.bank.allo.rest.entity.latest.LatestRatesResponse;
import com.bank.allo.rest.entity.supported.SupportedCurrenciesResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FinanceMapper {

    LatestRatesResponse toLatestRatesResponse(LatestRates domain);

    HistoricalRatesResponse toHistoricalRatesResponse(HistoricalRates domain);

    SupportedCurrenciesResponse toSupportedCurrenciesResponse(SupportedCurrencies domain);
}
