package com.allo.backendtest.service.impl;

import com.allo.backendtest.client.FrankfurterClient;
import com.allo.backendtest.dto.frankfurter.HistoricalDto;
import com.allo.backendtest.service.IdrDataFetcher;
import com.allo.backendtest.store.BaseStore;
import org.springframework.stereotype.Service;

@Service
public class HistoricalRateImpl implements IdrDataFetcher {

    private final BaseStore<HistoricalDto> historicalStore;
    private final FrankfurterClient frankfurterClient;

    public HistoricalRateImpl(BaseStore<HistoricalDto> historicalStore, FrankfurterClient frankfurterClient) {
        this.historicalStore = historicalStore;
        this.frankfurterClient = frankfurterClient;
    }

    @Override
    public void fetchAndStoreData() throws Exception {
        HistoricalDto data = frankfurterClient.getHistorical();
        if (data == null) throw new Exception("Failed : data is null");
        historicalStore.setData(data);
    }

}
