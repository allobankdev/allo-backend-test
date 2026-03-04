package com.idr_rate_aggregator_2.demo.services;

import com.idr_rate_aggregator_2.demo.idr_data_fetchers_interface.IDRDataFetcher;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FinanceDataService {

    // Map untuk menyimpan strategi berdasarkan resourceType
    private final Map<String, IDRDataFetcher> strategyMap = new HashMap<>();

    @Autowired
    public FinanceDataService(List<IDRDataFetcher> strategies) {
        // Spring secara otomatis menginject semua bean yang mengimplementasi IDRDataFetcher
        // ke dalam List ini

        // Memasukkan setiap strategi ke dalam Map
        for (IDRDataFetcher strategy : strategies) {
            strategyMap.put(strategy.getResourceType(), strategy);
            System.out.println("Registered strategy: " + strategy.getResourceType());
        }
    }

    // Method untuk mendapatkan strategi berdasarkan resourceType
    public IDRDataFetcher getStrategy(String resourceType) {
        IDRDataFetcher strategy = strategyMap.get(resourceType);
        if (strategy == null) {
            throw new IllegalArgumentException("Invalid resource type: " + resourceType);
        }
        return strategy;
    }

    // Optional: Method untuk mengecek semua strategi yang terdaftar
    @PostConstruct
    public void validateStrategies() {
        System.out.println("Available strategies: " + strategyMap.keySet());
    }
}