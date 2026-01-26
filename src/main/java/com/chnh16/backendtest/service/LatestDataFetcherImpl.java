package com.chnh16.backendtest.service;

import com.chnh16.backendtest.config.ApplicationConfig;
import com.chnh16.backendtest.exception.CommonException;
import com.chnh16.backendtest.model.response.LatestRateResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service("latest_idr_rates")
@RequiredArgsConstructor
public class LatestDataFetcherImpl implements DataFetcher {

    private final RestTemplate restTemplate;
    private final InMemoryStoreService storeService;
    private final ObjectMapper mapper;
    private final ApplicationConfig config;

    @Override
    public Object fetch() {
        try {
            if(storeService.contains("latest_idr_rates")) {
                log.info("Getting data from cache : latest_idr_rates");
                return mapper.readValue(storeService.get("latest_idr_rates"), LatestRateResponse.class);
            }
            log.info("Getting data from external API : latest_idr_rates");
            ResponseEntity<LatestRateResponse> response = restTemplate.getForEntity("/latest?base=IDR&symbols=USD", LatestRateResponse.class);
            LatestRateResponse result = response.getBody();
            result.setUSDBuySpreadIDR(calculateSpreadUSD(result.getRates().get("USD")));
            storeService.put("latest_idr_rates", mapper.writeValueAsString(result));
            return result;
        } catch (JsonProcessingException e) {
            throw new CommonException(e.getMessage());
        } catch (HttpClientErrorException e) {
            throw new CommonException(e.getResponseBodyAsString());
        }
    }

    /*
    Spread Factor = (Sum of Unicode Values % 1000) / 100000.0
    USD_BuySpread_IDR = (1 / Rate_USD) * (1 + Spread Factor)
     */
    private Double calculateSpreadUSD(Double rateUSD) {
        int unicodeValue = config.getGithubUsername().chars().sum();
        log.info(String.format("UNICODE VALUE : %s", unicodeValue));
        double spreadFactor = (unicodeValue % 1000) / 100000.0;
        return (1 / rateUSD) * (1 + spreadFactor);
    }
}
