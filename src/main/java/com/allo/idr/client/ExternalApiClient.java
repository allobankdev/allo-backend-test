package com.allo.idr.client;

import com.allo.idr.config.ExternalApiProperties;
import com.allo.idr.exception.ExternalApiException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@Component
public class ExternalApiClient {
    private final RestTemplate resT;
    private final ExternalApiProperties expProps;

    public ExternalApiClient(RestTemplate resT, ExternalApiProperties expProps) {
        this.resT = resT;
        this.expProps = expProps;
    }

    public Map<String, Object> getLatestBaseIdr() {
        URI uri = UriComponentsBuilder.fromHttpUrl(expProps.getBaseUrl())
                .path("/latest")
                .queryParam("base", "IDR")
                .build().toUri();
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> res = resT.getForObject(uri, Map.class);
            return res;
        } catch (RestClientException e) {
            throw new ExternalApiException("GET" + uri + "failed", e);
        }
    }

    public Map<String, Object> getHistoricalIdrToUsd(String from, String to) {
        String range = from + ".." + to;
        URI uri = UriComponentsBuilder.fromHttpUrl(expProps.getBaseUrl())
                .path("/" + range)
                .queryParam("from", "IDR")
                .queryParam("to","USD")
                .build().toUri();
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> res = resT.getForObject(uri, Map.class);
            return res;
        } catch (RestClientException e) {
            throw new ExternalApiException("GET" + uri + "failed", e);
        }
    }

    public Map<String, String> getCurrencies() {
        URI uri = UriComponentsBuilder.fromHttpUrl(expProps.getBaseUrl())
                .path("/currencies")
                .build().toUri();
        try {
            @SuppressWarnings("unchecked")
            Map<String, String> res = resT.getForObject(uri, Map.class);
            return res;
        } catch (RestClientException e) {
            throw new ExternalApiException("GET" + uri + "failed", e);
        }
    }
}
