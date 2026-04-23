package com.allobank.finnance.allobankfinance.integration.impl;

import com.allobank.finnance.allobankfinance.config.FrankfurterProperties;
import com.allobank.finnance.allobankfinance.dto.frankfurter.HistoricalRatesResponse;
import com.allobank.finnance.allobankfinance.dto.frankfurter.LatestRatesResponse;
import com.allobank.finnance.allobankfinance.exception.FrankfurterException;
import com.allobank.finnance.allobankfinance.integration.FrankfurterIntegrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class FrankfurterIntegrationServiceImpl implements FrankfurterIntegrationService {

    private final RestTemplate restTemplate;
    private final FrankfurterProperties frankfurterProperties;


    @Override
    public LatestRatesResponse getLatestUsdRates(String baseCurrency) {
        log.info("Getting latest usd rates for currency {}", baseCurrency);
        try {
            log.info("getLatestUsdRates");
            URI uri = UriComponentsBuilder
                    .fromUriString(frankfurterProperties.getLatestUrl())
                    .queryParam("base", baseCurrency)
                    .build()
                    .toUri();
            return restTemplate.getForObject(uri, LatestRatesResponse.class);
        }catch (Exception e){
            log.error("getLatestUsdRates Exception", e);
            throw new FrankfurterException("FrankfurterException Timeout");
        }
    }

    @Override
    public HistoricalRatesResponse getHistoricalRates(String startDate, String endDate, String baseCurrency, String to) {
        log.info("getHistoricalRates");
        try {
            String dateRange = startDate + ".." + endDate;
            URI uri = UriComponentsBuilder.fromUriString(frankfurterProperties.getBaseUrl())
                    .pathSegment(dateRange)
                    .queryParam("base", baseCurrency)
                    .queryParam("to", to)
                    .build()
                    .toUri();

            return restTemplate.getForObject(
                    uri,
                    HistoricalRatesResponse.class
            );
        }catch (Exception e){
            log.error("getHistoricalRates Exception", e);
            throw new FrankfurterException("FrankfurterException Timeout");
        }
    }


    @Override
    public Map<String, String> getSupportedCurrencies() {
        log.info("getSupportedCurrencies");
        try {
            return restTemplate.getForObject(
                    frankfurterProperties.getCurrenciesUrl(),
                    Map.class);
        }catch (Exception e){
            log.error("getSupportedCurrencies Exception", e);
            throw new FrankfurterException("FrankfurterException Timeout");
        }
    }
}
