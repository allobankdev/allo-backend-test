package com.allobank.idr_rate_aggregator.config;

import com.allobank.idr_rate_aggregator.model.ResourceType;
import com.allobank.idr_rate_aggregator.service.strategy.IDRDataFetcherStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Konfigurasi untuk mendaftarkan semua Strategy
 * ke dalam Map berdasarkan ResourceType.
 *
 * Tujuannya agar controller/service tidak perlu
 * menggunakan if/else atau switch-case.
 */
@Configuration
public class StrategyConfig {

    /**
     * Spring akan otomatis inject semua bean yang
     * mengimplementasikan IDRDataFetcherStrategy.
     *
     * Kemudian kita ubah menjadi Map<ResourceType, Strategy>
     */
    @Bean
    public Map<ResourceType, IDRDataFetcherStrategy> strategyMap(
            List<IDRDataFetcherStrategy> strategies) {

        return strategies.stream()
                .collect(Collectors.toMap(
                        IDRDataFetcherStrategy::getSupportedType,
                        Function.identity()
                ));
    }
}