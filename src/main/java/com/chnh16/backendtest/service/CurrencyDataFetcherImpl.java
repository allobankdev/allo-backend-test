package com.chnh16.backendtest.service;

import com.chnh16.backendtest.exception.CommonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service("supported_currencies")
@RequiredArgsConstructor
public class CurrencyDataFetcherImpl implements DataFetcher {

    private final RestTemplate restTemplate;
    private final InMemoryStoreService storeService;
    private final ObjectMapper mapper;

    @Override
    public Object fetch() {
        try {
            if(storeService.contains("supported_currencies")) {
                log.info("Getting data from cache : supported_currencies");
                return mapper.readValue(storeService.get("supported_currencies"), new TypeReference<HashMap<String, String>>() {
                });
            }
            log.info("Getting data from external API : supported_currencies");
            ResponseEntity<Map<String, String>> response = restTemplate.exchange("/currencies", HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, String>>() {});
            Map<String, String> result = response.getBody();
            storeService.put("supported_currencies", mapper.writeValueAsString(result));
            return result;
        } catch (JsonProcessingException e) {
            throw new CommonException(e.getMessage());
        } catch (HttpClientErrorException e) {
            throw new CommonException(e.getResponseBodyAsString());
        }
    }
}
