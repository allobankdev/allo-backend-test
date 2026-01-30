package com.example.allobank.strategy;

import com.example.allobank.dto.LatestIDRResponse;
import com.example.allobank.dto.HistoricalRatesResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class FrankfurterServiceImpl implements FrankfurterService{

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public LatestIDRResponse getLatestBaseIDR(String url) {
        try {
            return restTemplate.getForObject(url, LatestIDRResponse.class);
        } catch (HttpStatusCodeException e) {
            throw e;
        } catch (ResourceAccessException e) {
            throw new RuntimeException("Gagal koneksi ke external API", e);
        }

    }

    @Override
    public HistoricalRatesResponse getHistoricalRatesDate(String url) {
        try {
            return restTemplate.getForObject(url, HistoricalRatesResponse.class);
        }catch (HttpStatusCodeException e) {
            throw e;
        } catch (ResourceAccessException e) {
            throw new RuntimeException("Gagal koneksi ke external API", e);
        }
    }

    @Override
    public Map<String, String> getCurrencies(String url) {
       try {
           ResponseEntity<Map> response =
                   restTemplate.getForEntity(url, Map.class);

           Map<String, String> currencies = response.getBody();
           return currencies;
       }catch (HttpStatusCodeException e) {
           throw e;
       } catch (ResourceAccessException e) {
           throw new RuntimeException("Gagal koneksi ke external API", e);
       }
    }
}
