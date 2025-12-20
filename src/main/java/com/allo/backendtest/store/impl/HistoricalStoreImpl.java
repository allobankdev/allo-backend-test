package com.allo.backendtest.store.impl;

import com.allo.backendtest.dto.frankfurter.HistoricalDto;
import com.allo.backendtest.store.BaseStore;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component("historical_idr_usd")
public class HistoricalStoreImpl implements BaseStore<HistoricalDto> {

    private final CompletableFuture<HistoricalDto> completable = new CompletableFuture<>();

    @Override
    public HistoricalDto getData() {
        return completable.join();
    }

    @Override
    public void setData(HistoricalDto data) {
        completable.complete(data);
    }
}
