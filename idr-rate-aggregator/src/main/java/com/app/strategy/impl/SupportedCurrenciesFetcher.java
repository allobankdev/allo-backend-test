package com.app.strategy.impl;

import com.app.config.properties.ApiClientProperties;
import com.app.dao.ExchangeAggregatorDao;
import com.app.dto.SupportedCurrenciesResponseDto;
import com.app.error.JsonParsingException;
import com.app.error.NotFoundError;
import com.app.error.ServerStatusException;
import com.app.model.ExchangeAggregator;
import com.app.service.impl.ExchangeAggregatorServiceImpl;
import com.app.strategy.IDRDataFetcher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.UnknownHostException;
import java.util.Objects;

@Slf4j
@Component
public class SupportedCurrenciesFetcher implements IDRDataFetcher<SupportedCurrenciesResponseDto> {
    private final ExchangeAggregatorServiceImpl exchangeAggregatorService;

    private final RestTemplate restTemplate;

    private final ApiClientProperties apiClientProperties;

    private final ObjectMapper objectMapper;

    public SupportedCurrenciesFetcher(ExchangeAggregatorServiceImpl exchangeAggregatorService, RestTemplate restTemplate, ApiClientProperties apiClientProperties, ObjectMapper objectMapper) {
        this.exchangeAggregatorService = exchangeAggregatorService;
        this.restTemplate = restTemplate;
        this.apiClientProperties = apiClientProperties;
        this.objectMapper = objectMapper;
    }

    @Override
    public void save(ExchangeAggregator exchangeAggregator) {
        exchangeAggregatorService.save(exchangeAggregator);
    }

    @Override
    public String getResourceType() {
        return "supported_currencies";
    }

    @Override
    public SupportedCurrenciesResponseDto find(String id) {

        ExchangeAggregator exchangeAggregator = null;
        SupportedCurrenciesResponseDto supportedCurrenciesResponseDto = null;

        try {
            exchangeAggregator = exchangeAggregatorService.findById(id);


            if (!Objects.isNull(exchangeAggregator.getIsErr()) && exchangeAggregator.getIsErr() == "Y" ){

                //check if the error is http error
                if (!Objects.isNull(exchangeAggregator.getErrCode())){
                    if (exchangeAggregator.getErrCode() == 504){
                        throw new ServerStatusException("Gateway Timeout");
                    } else if (exchangeAggregator.getErrCode() == 503) {
                        throw new ServerStatusException("Service Unavailable");
                    } else if (exchangeAggregator.getErrCode() == 502) {
                        throw new ServerStatusException(exchangeAggregator.getErrMessage());
                    }else if (exchangeAggregator.getErrCode() == 500) {
                        throw new ServerStatusException("Internal Server Error");
                    }else if (exchangeAggregator.getErrCode() == 404){
                        throw new NotFoundError("Not Found");
                    }else if (exchangeAggregator.getErrCode() == 400){
                        log.error("bad request");
                        throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
                    }
                }else{
                    if (exchangeAggregator.getErrMessage().contains("json")){
                        log.error("json error");
                        throw new JsonParsingException(exchangeAggregator.getErrMessage());
                    }else{
                        log.error("runtime exception");
                        throw new RuntimeException(exchangeAggregator.getErrMessage());
                    }
                }
            }

            supportedCurrenciesResponseDto = objectMapper.readValue(exchangeAggregator.getData(), SupportedCurrenciesResponseDto.class);
            return supportedCurrenciesResponseDto;
        }catch (JsonProcessingException ex){
            throw new JsonParsingException(ex.getMessage());
        }catch (Exception exception){
            log.error("exception");
            log.error("getCause: {}", exception.getCause());
            log.error("getStackTrace: {}", exception.getStackTrace());
            throw exception;
        }
    }

    @Override
    public SupportedCurrenciesResponseDto execute() {
        ExchangeAggregator exchangeAggregator = new ExchangeAggregator();
        String url = apiClientProperties.getBaseUrl() + "/currencies";
        try {
            log.info("/fetchSupportedCurrencies");

            SupportedCurrenciesResponseDto response = restTemplate.getForObject(url, SupportedCurrenciesResponseDto.class);

            exchangeAggregator.setData(objectMapper.writeValueAsString(response));
            exchangeAggregator.setIsErr("N");

            return response;

        }catch (HttpServerErrorException ex){
            exchangeAggregator.setIsErr("Y");
            if (ex.getRawStatusCode() == 504){
                exchangeAggregator.setErrCode(504);
                exchangeAggregator.setErrMessage("Gateway Timeout");
            }else if (ex.getRawStatusCode() == 503){
                exchangeAggregator.setErrCode(503);
                exchangeAggregator.setErrMessage("Service Unavailable");
            }else{
                exchangeAggregator.setErrCode(500);
                exchangeAggregator.setErrMessage("Internal Server Error");
            }
        }catch (HttpClientErrorException ex){
            exchangeAggregator.setIsErr("Y");
            if (ex.getRawStatusCode() == 404){
                exchangeAggregator.setErrCode(404);
                exchangeAggregator.setErrMessage("Not Found");
            }else{
                exchangeAggregator.setErrCode(400);
                exchangeAggregator.setErrMessage("Bad Request");
            }
        }catch (ResourceAccessException ex){
            exchangeAggregator.setIsErr("Y");
            if (ex.getCause() instanceof UnknownHostException) {

                // Specific handling for DNS/host not found
                log.error("DNS/host not found");

                exchangeAggregator.setErrCode(502);
                exchangeAggregator.setErrMessage("DNS/host not found: " + url);
            }else{
                exchangeAggregator.setErrCode(502);
                exchangeAggregator.setErrMessage(ex.getMessage());
            }

        }catch (JsonProcessingException e) {
            exchangeAggregator.setIsErr("Y");
            exchangeAggregator.setErrMessage("Failed to parse JSON data");
        }catch (Exception exception){
            exchangeAggregator.setIsErr("Y");
            exchangeAggregator.setErrMessage(exception.getMessage());
        }finally {
            exchangeAggregator.setId(getResourceType());
            exchangeAggregatorService.save(exchangeAggregator);
        }
        return null;
    }
}
