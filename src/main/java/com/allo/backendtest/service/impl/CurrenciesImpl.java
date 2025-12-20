package com.allo.backendtest.service.impl;

import com.allo.backendtest.client.FrankfurterClient;
import com.allo.backendtest.dto.frankfurter.CurrenciesDto;
import com.allo.backendtest.service.IdrDataFetcher;
import com.allo.backendtest.store.BaseStore;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@Service
public class CurrenciesImpl implements IdrDataFetcher {

    private final BaseStore<CurrenciesDto> currenciesStore;
    private final FrankfurterClient frankfurterClient;
    private final ObjectMapper mapper;

    public CurrenciesImpl(BaseStore<CurrenciesDto> currenciesStore, FrankfurterClient frankfurterClient, ObjectMapper mapper) {
        this.currenciesStore = currenciesStore;
        this.frankfurterClient = frankfurterClient;
        this.mapper = mapper;
    }

    @Override
    public void fetchAndStoreData() throws Exception {
        Map<String, Object> data = frankfurterClient.getCurrencies();
        if (data == null) throw new Exception("Failed : data is null");

        var wrapper = Map.of("mapCurrencies", data);
        currenciesStore.setData(mapper.convertValue(wrapper, CurrenciesDto.class));
    }

}
