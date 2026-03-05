package com.ade.exchangerateagregator.domain.service;

import com.ade.exchangerateagregator.application.dto.out.HistorycalResponse;
import com.ade.exchangerateagregator.application.dto.out.LatesIdrRateExternalResponse;

import java.util.Map;

public interface ExternalService {
    LatesIdrRateExternalResponse getLatesIdrRate(String currency);
    HistorycalResponse getHistory(String fromCurrency, String toCurrency, String startDate, String endDate);
    Map<String, String> getCurrencies();
}
