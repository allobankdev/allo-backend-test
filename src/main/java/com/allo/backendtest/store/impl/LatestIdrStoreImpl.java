package com.allo.backendtest.store.impl;

import com.allo.backendtest.dto.frankfurter.LatestDto;
import com.allo.backendtest.store.BaseStore;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component("latest_idr_rates")
public class LatestIdrStoreImpl implements BaseStore<LatestDto> {

    private final CompletableFuture<LatestDto> completable = new CompletableFuture<>();

    @Override
    public LatestDto getData() {
        return completable.join();
    }

    @Override
    public void setData(LatestDto data) {
        completable.complete(data);
    }
}
