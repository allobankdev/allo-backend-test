package com.example.financedata.fetcher;

import com.example.financedata.dto.HistoricalDto;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;

import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.tcp.TcpClient;

import java.util.Map;
import reactor.netty.http.client.HttpClient;
import java.time.Duration;

public class HistoricalIdrUsdFetcher implements IDRDataFetcher {

    private final WebClient webClient;

    // using the exact date range specified in the spec
    private static final String RANGE = "/2024-01-01..2024-01-05";

    public HistoricalIdrUsdFetcher(WebClient webClient) {
         HttpClient httpClient = HttpClient.create()
                .option(io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000) 
                .responseTimeout(Duration.ofSeconds(60)); 
                
        this.webClient = WebClient.builder()
                .baseUrl("https://api.frankfurter.app")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Override
    public Mono<Object> fetch() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path(RANGE).queryParam("from", "IDR").queryParam("to", "USD").build())
                .retrieve()
                .bodyToMono(Map.class)
                .map(map -> {
                    HistoricalDto dto = new HistoricalDto();
                    dto.setFrom("IDR");
                    dto.setTo("USD");
                    dto.setRaw(map);
                    return dto;
                });
    }

    @Override
    public String resourceKey() {
        return "historical_idr_usd";
    }
}
