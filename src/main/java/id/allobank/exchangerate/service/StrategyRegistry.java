package id.allobank.exchangerate.service;

import id.allobank.exchangerate.exception.ApiException;
import id.allobank.exchangerate.strategy.IDRDataFetcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StrategyRegistry {

    private final Map<String, IDRDataFetcher> strategies;

    public StrategyRegistry(List<IDRDataFetcher> list) {
        this.strategies = list.stream()
                .collect(Collectors.toMap(IDRDataFetcher::getType, s -> s));
    }

    public IDRDataFetcher get(String type) {
        IDRDataFetcher strategy = strategies.get(type);

        if (strategy == null) {
            throw new ApiException("Invalid type");
        }

        return strategy;
    }

    public Set<String> getSupportedTypes() {
        return strategies.keySet();
    }
}
