package id.co.microservice.currency.currency_service;

import id.co.microservice.currency.currency_service.dto.CurrencyResponseDto;
import id.co.microservice.currency.currency_service.service.CurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class CacheWarmupRunner implements ApplicationRunner {

    private final CurrencyService currencyService;

    @Autowired
    public CacheWarmupRunner(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<String> warmupResources = List.of(
                "supported_currencies",
                "latest_idr_usd",
                "historical_idr_usd"
        );

        for(String resourceType : warmupResources) {
            try {
                CurrencyResponseDto dto = currencyService.executeStrategy(resourceType);
                log.info("Cache pre-warmed for resourceType={} with value={}", resourceType, dto);
            } catch (Exception e) {
                log.warn("Failed to pre-warm cache: {}", e.getMessage(), e);
            }
        }
    }

}
