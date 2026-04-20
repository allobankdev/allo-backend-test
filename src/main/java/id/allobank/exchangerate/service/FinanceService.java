package id.allobank.exchangerate.service;

import id.allobank.exchangerate.strategy.IDRDataFetcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FinanceService {

    private final Map<String, IDRDataFetcher> strategyMap;

    public FinanceService(List<IDRDataFetcher> strategies){
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(IDRDataFetcher::getType, s -> s));
    }

    public Object getData(String type){
        IDRDataFetcher strategy = strategyMap.get(type);
        if(strategy == null){
            throw new RuntimeException("Invalid resourceType");
        }
        return strategy.fetch();
    }
}
