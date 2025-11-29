package com.allobank.backendtest.health;

import com.allobank.backendtest.service.ImmutableFinanceStore;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component("financeStore")
public class StoreHealthIndicator implements HealthIndicator {
    private final ImmutableFinanceStore store;

    public StoreHealthIndicator(ImmutableFinanceStore store) {
        this.store = store;
    }

    @Override
    public Health health() {
        if (store.isInitialized()) {
            return Health.up().withDetail("initialized", true).build();
        } else {
            return Health.down().withDetail("initialized", false).withDetail("reason", "store not initialized").build();
        }
    }
}