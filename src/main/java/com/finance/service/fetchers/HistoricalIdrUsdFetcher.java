package com.finance.service.fetchers;

import com.finance.client.FrankfurterClient;
import com.finance.constant.AppConstant;
import com.finance.dto.external.HistoricalRateResponse;
import com.finance.dto.internal.HistoricalRateInfoResponse;
import com.finance.exception.ExternalServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class HistoricalIdrUsdFetcher implements DataFetcher {

    private final FrankfurterClient client;

    public HistoricalIdrUsdFetcher(FrankfurterClient client) {
        this.client = client;
    }

    @Override
    public String resourceType() {
        return "historical_idr_usd";
    }

    @Override
    public List<HistoricalRateInfoResponse> fetch() {

        HistoricalRateResponse response = client
                .getHistoricalIdrUsd()
                .blockOptional()
                .orElseThrow(() -> new ExternalServiceException(
                        AppConstant.NO_RESPONSE_FROM_API_MESSAGE,
                        HttpStatus.INTERNAL_SERVER_ERROR
                ));

        return response.getRates().entrySet().stream()
                .map(e -> new HistoricalRateInfoResponse(
                        e.getKey(),          // string tanggal dari API
                        e.getValue().get("USD")
                ))
                .toList();
    }
}