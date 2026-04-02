package com.allobank.frankfurter.service.strategy;

import com.allobank.frankfurter.client.WebClientFactoryBean;
import com.allobank.frankfurter.model.DataResult;
import com.allobank.frankfurter.model.HistoricalRatesResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class HistoricalRatesFetcher implements IDRDataFetcher {

    private final WebClient webClient;
    private final String historicalRatesPath;
    private final String historicalRatesStartDate;
    private final String historicalRatesEndDate;

    public HistoricalRatesFetcher(WebClientFactoryBean webClientFactoryBean,
                                   @Value("${frankfurter.api.historical-rates-path}") String historicalRatesPath,
                                   @Value("${frankfurter.api.historical-rates-start-date}") String historicalRatesStartDate,
                                   @Value("${frankfurter.api.historical-rates-end-date}") String historicalRatesEndDate) throws Exception {
        this.webClient = webClientFactoryBean.getObject();
        this.historicalRatesPath = historicalRatesPath;
        this.historicalRatesStartDate = historicalRatesStartDate;
        this.historicalRatesEndDate = historicalRatesEndDate;
    }

    @Override
    public DataResult fetchData() {
        String path = String.format(historicalRatesPath, historicalRatesStartDate, historicalRatesEndDate);
        HistoricalRatesResponse response = webClient.get()
                .uri(path)
                .retrieve()
                .bodyToMono(HistoricalRatesResponse.class)
                .block();

        return new DataResult(getResourceType(), response);
    }

    @Override
    public String getResourceType() {
        return "historical_idr_usd";
    }
}