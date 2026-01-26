package com.chnh16.backendtest.service;

import com.chnh16.backendtest.exception.CommonException;
import com.chnh16.backendtest.model.response.HistoricalResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service("historical_idr_usd")
@RequiredArgsConstructor
public class HistoricalDataFetcherImpl implements DataFetcher {

    private final RestTemplate restTemplate;
    private final InMemoryStoreService storeService;
    private final ObjectMapper mapper;

    @Override
    public Object fetch() {
        try {
            if(storeService.contains("historical_idr_usd")) {
                log.info("Getting data from cache : historical_idr_usd");
                return mapper.readValue(storeService.get("historical_idr_usd"), HistoricalResponse.class);
            }
            log.info("Getting data from external API : historical_idr_usd");
            ResponseEntity<HistoricalResponse> response = restTemplate.getForEntity("/2024-01-01..2024-01-05?base=IDR&symbols=USD", HistoricalResponse.class);
            HistoricalResponse result = response.getBody();
            storeService.put("historical_idr_usd", mapper.writeValueAsString(result));
            return result;
        } catch (JsonProcessingException e) {
            throw new CommonException(e.getMessage());
        } catch (HttpClientErrorException e) {
            throw new CommonException(e.getResponseBodyAsString());
        }
    }
}
