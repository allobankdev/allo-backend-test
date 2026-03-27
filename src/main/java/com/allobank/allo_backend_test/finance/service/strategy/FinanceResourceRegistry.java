package com.allobank.allo_backend_test.finance.service.strategy;

import com.allobank.allo_backend_test.finance.service.strategy.finance_resource.FinanceResourceHandler;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FinanceResourceRegistry {
    private final Map<String, FinanceResourceHandler> handlerMap;

    public FinanceResourceRegistry(List<FinanceResourceHandler> handlers) {
        this.handlerMap = new HashMap<>();
        for (FinanceResourceHandler handler : handlers) {
            handlerMap.put(handler.resourceType(), handler);
        }
    }

    public Map<String, FinanceResourceHandler> asMap() {
        return handlerMap;
    }

    public FinanceResourceHandler get(String resourceType) {
        return handlerMap.get(resourceType);
    }
}