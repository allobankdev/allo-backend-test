package com.app.controller;

import com.app.dto.ApiClientResponseDto;
import com.app.error.NotFoundError;
import com.app.error.ServerStatusException;
import com.app.service.impl.ExchangeAggregatorServiceImpl;
import com.app.strategy.IDRDataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping(value = "/api/finance/data")
public class ExchangeAggregatorController {

    private final ExchangeAggregatorServiceImpl exchangeAggregatorServiceImpl;
    private final Map<String, IDRDataFetcher<?>> fetcherMap;

    public ExchangeAggregatorController(ExchangeAggregatorServiceImpl exchangeAggregatorServiceImpl, Map<String, IDRDataFetcher<?>> fetcherMap) {
        this.exchangeAggregatorServiceImpl = exchangeAggregatorServiceImpl;
        this.fetcherMap = fetcherMap;
    }

    @GetMapping(
            value = "/{resourceType}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> get(@PathVariable String resourceType){


        try{
            ApiClientResponseDto<Object> data = null;
            for (Map.Entry<String, IDRDataFetcher<?>> a: fetcherMap.entrySet()) {
                IDRDataFetcher<?> fetcher = a.getValue();
                if (fetcher.getResourceType().equals(resourceType)){
                    data = new ApiClientResponseDto<>(HttpStatus.OK.toString(), fetcher.find(resourceType));
                    break;
                }
            }

            if (Objects.isNull(data)){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiClientResponseDto<>(HttpStatus.BAD_REQUEST.toString(), "The given path URL is invalid"));
            }

            return ResponseEntity.ok(data);

        }catch (ServerStatusException responseStatusException){
            if (responseStatusException.getMessage().contains("Timeout")){
                return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body( new ApiClientResponseDto<>(HttpStatus.GATEWAY_TIMEOUT.toString(), responseStatusException.getMessage()));
            }else if (responseStatusException.getMessage().contains("Unavailable")){
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ApiClientResponseDto<>(HttpStatus.SERVICE_UNAVAILABLE.toString(), responseStatusException.getMessage()));
            }else if (responseStatusException.getMessage().contains("DNS")){
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ApiClientResponseDto<>(HttpStatus.BAD_GATEWAY.toString(), responseStatusException.getMessage()));
            }else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiClientResponseDto<>(HttpStatus.INTERNAL_SERVER_ERROR.toString(), responseStatusException.getMessage()));
            }
        }catch(NotFoundError notFoundError){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiClientResponseDto<>(HttpStatus.NOT_FOUND.toString(), notFoundError.getMessage()));
        }catch (HttpClientErrorException exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiClientResponseDto<>(HttpStatus.BAD_REQUEST.toString(), "BAD REQUEST"));
        }catch (Exception exception){
            return ResponseEntity.badRequest().body(new ApiClientResponseDto<>(HttpStatus.BAD_REQUEST.toString(), exception.getMessage()));
        }

    }


}
