package com.aryaevan.allo.config;

import com.aryaevan.allo.client.FrankfurterClientFactoryBean;
import com.aryaevan.allo.strategy.IDRDataFetcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Configuration class for bootstrapping the Strategy Pattern components.
 * Creates a map-based strategy lookup to avoid if/else logic in the controller.
 * Also registers the WebClient FactoryBean for dependency injection.
 */
@Configuration
public class StrategyConfig {
    
    /**
     * Registers the WebClient FactoryBean to create WebClient instances.
     * This demonstrates the FactoryBean pattern for complex bean creation.
     * 
     * @return FrankfurterClientFactoryBean instance
     */
    @Bean
    public FrankfurterClientFactoryBean frankfurterClientFactory() {
        return new FrankfurterClientFactoryBean();
    }
    
    /**
     * Registers the WebClient bean created by the FactoryBean.
     * 
     * @param factory The FrankfurterClientFactoryBean
     * @return WebClient instance
     * @throws Exception if factory fails to create the client
     */
    @Bean
    public WebClient webClient(FrankfurterClientFactoryBean factory) throws Exception {
        return factory.getObject();
    }
    
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

