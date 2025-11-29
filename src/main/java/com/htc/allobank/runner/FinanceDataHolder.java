package com.htc.allobank.runner;

import com.htc.allobank.dto.FinanceDataStore;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

@Component
public class FinanceDataHolder {
    private final AtomicReference<FinanceDataStore> ref = new AtomicReference<>();

    public void setStore(FinanceDataStore store) {
        ref.set(store);
    }

    public FinanceDataStore getStore() {
        return ref.get();
    }
}
