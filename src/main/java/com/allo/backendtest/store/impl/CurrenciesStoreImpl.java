package com.allo.backendtest.store.impl;

import com.allo.backendtest.dto.frankfurter.CurrenciesDto;
import com.allo.backendtest.store.BaseStore;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component("supported_currencies")
public class CurrenciesStoreImpl implements BaseStore<CurrenciesDto> {

    private final CompletableFuture<CurrenciesDto> completable = new CompletableFuture<>();

    @Override
    public CurrenciesDto getData() {
        return completable.join();
    }

    @Override
    public void setData(CurrenciesDto data) {
        completable.complete(data);
    }
}
