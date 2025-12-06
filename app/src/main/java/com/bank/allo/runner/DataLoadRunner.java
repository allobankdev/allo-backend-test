package com.bank.allo.runner;

import com.bank.allo.repository.inbound.DataStore;
import com.bank.allo.usecase.idr.FetchIdrDataUseCase;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import java.util.HashMap;
import java.util.Map;

public class DataLoadRunner implements ApplicationRunner {

    private final FetchIdrDataUseCase fetchUseCase;
    private final DataStore store;

    public DataLoadRunner(FetchIdrDataUseCase fetchUseCase, DataStore store) {
        this.fetchUseCase = fetchUseCase;
        this.store = store;
    }

    @Override
    public void run(ApplicationArguments args) {

        Map<String, Object> data = new HashMap<>();

        data.put("latest_idr_rates",
                fetchUseCase.execute(
                        FetchIdrDataUseCase.InputValues.builder()
                                .resourceType("latest_idr_rates")
                                .build()
                ).getResult()
        );

        data.put("historical_idr_usd",
                fetchUseCase.execute(
                        FetchIdrDataUseCase.InputValues.builder()
                                .resourceType("historical_idr_usd")
                                .build()
                ).getResult()
        );

        data.put("supported_currencies",
                fetchUseCase.execute(
                        FetchIdrDataUseCase.InputValues.builder()
                                .resourceType("supported_currencies")
                                .build()
                ).getResult()
        );

        store.initialize(data);
    }
}
