package com.allobank.allo_backend_test.finance.service.strategy;

import com.allobank.allo_backend_test.finance.service.strategy.finance_resource.FinanceResourceHandler;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Getter
public class FinanceResourceRegistry {
    private final Map<String, FinanceResourceHandler> handlerMap;

    public FinanceResourceRegistry(List<FinanceResourceHandler> handlers) {
        this.handlerMap = new HashMap<>();
        for (FinanceResourceHandler handler : handlers) {
            handlerMap.put(handler.resourceType(), handler);
        }
    }

    public FinanceResourceHandler get(String resourceType) {
        return handlerMap.get(resourceType);
    }
}