package com.ade.exchangerateagregator.adapter.implementation;

import com.ade.exchangerateagregator.adapter.out.external.ExternalServiceImpl;
import com.ade.exchangerateagregator.application.dto.in.FinanceBaseResponse;
import com.ade.exchangerateagregator.application.dto.in.HistoricalResponse;
import com.ade.exchangerateagregator.application.dto.out.HistorycalResponse;
import com.ade.exchangerateagregator.domain.constant.ResourceType;
import com.ade.exchangerateagregator.domain.service.FinanceBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryService implements FinanceBaseService {
    private final ExternalServiceImpl externalService;

    @Override
    public ResourceType getSourceType() {
        return ResourceType.historical_idr_usd;
    }

    @Override
    public List<? extends FinanceBaseResponse> fetchData() {
        var startDate = "2024-01-01";
        var endDate = "2024-01-05";
        var fromCurrency = "IDR";
        var toCurrency = "USD";

        var historyResponse = externalService.getHistory(fromCurrency, toCurrency, startDate, endDate);
        return mapHistoryResponse(historyResponse);
    }

    private List<? extends FinanceBaseResponse> mapHistoryResponse(HistorycalResponse response) {
        List<FinanceBaseResponse> result = new ArrayList<>();
        result.add(HistoricalResponse.builder()
                .amount(response.getAmount())
                .baseCurrency(response.getBaseCurrency())
                .endDate(response.getEndDate())
                .startDate(response.getStartDate())
                .rates(response.getRates())
                .build());
        return result;
    }
}