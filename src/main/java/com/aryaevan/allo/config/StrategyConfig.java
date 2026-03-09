package com.aryaevan.allo.config;

import com.aryaevan.allo.strategy.IDRDataFetcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Configuration class for bootstrapping the Strategy Pattern components.
 * Creates a map-based lookup of strategies to avoid conditional logic in the controller.
 */
@Configuration
public class StrategyConfig {
    
    /**
     * Creates a map of resource types to their corresponding strategy implementations.
     * This map is used by the controller for dynamic strategy selection.
     * 
     * @param strategies List of all IDRDataFetcher strategy implementations (auto-wired by Spring)
     * @return Map with resource type as key and strategy as value
     */
    @Bean
    public Map<String, IDRDataFetcher> strategyMap(List<IDRDataFetcher> strategies) {
        return strategies.stream()
                .collect(Collectors.toMap(
                    IDRDataFetcher::getResourceType,
                    strategy -> strategy
                ));
    }
}
