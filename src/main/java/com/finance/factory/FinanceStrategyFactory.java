package com.finance.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.finance.strategy.FinanceStrategy;

@Component
public class FinanceStrategyFactory {

    private final Map<String, FinanceStrategy> strategyMap;

    public FinanceStrategyFactory(List<FinanceStrategy> strategies){
        
        strategyMap = new HashMap<>();

        for(FinanceStrategy strategy : strategies){
            strategyMap.put(strategy.getType(), strategy);
        }
    }

    public FinanceStrategy getStrategy(String type){
        FinanceStrategy strategy = strategyMap.get(type);

        if(strategy == null) {
            throw new IllegalArgumentException("Invalid type: " + type);
        }

        return strategy;
    }
}
