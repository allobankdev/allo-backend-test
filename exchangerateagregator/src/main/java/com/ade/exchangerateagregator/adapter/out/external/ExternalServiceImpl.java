package com.ade.exchangerateagregator.adapter.out.external;

import com.ade.exchangerateagregator.application.dto.out.HistorycalResponse;
import com.ade.exchangerateagregator.application.dto.out.LatesIdrRateExternalResponse;
import com.ade.exchangerateagregator.domain.service.ExternalService;
import com.ade.exchangerateagregator.exception.ExternalApiCallException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExternalServiceImpl implements ExternalService {
    private final FrankfurterClient frankfurterClient;

    @Override
    public LatesIdrRateExternalResponse getLatesIdrRate(String currency) {
        try {
            return frankfurterClient.getLatesIdrRate(currency);
        }catch (WebClientResponseException e){
            var msg = String.format("Failed to call external api. Status: %d",e.getStatusCode());
            throw new ExternalApiCallException(msg,e);
        }catch (Exception e) {
            throw new ExternalApiCallException("Unexpected error", e);
        }
    }

    @Override
    public HistorycalResponse getHistory(String fromCurrency, String toCurrency, String startDate, String endDate) {
        try {
            return frankfurterClient.getHistory(fromCurrency,toCurrency,startDate,endDate);
        }catch (WebClientResponseException e){
            var msg = String.format("Failed to call external api. Status: %d",e.getStatusCode());
            throw new ExternalApiCallException(msg,e);
        }catch (Exception e) {
            throw new ExternalApiCallException("Unexpected error", e);
        }
    }

    @Override
    public Map<String, String> getCurrencies() {
        try {
            return frankfurterClient.getCurrencies();
        }catch (WebClientResponseException e){
            var msg = String.format("Failed to call external api. Status: %d",e.getStatusCode());
            throw new ExternalApiCallException(msg,e);
        }catch (Exception e) {
            throw new ExternalApiCallException("Unexpected error", e);
        }
    }
}
