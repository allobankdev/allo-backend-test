package com.sdewa.IdrRateAggregator.bootstrap;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sdewa.IdrRateAggregator.services.AppDataStore;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@SpringBootTest
public class StartupDataLoaderTest {

    @Autowired
    private AppDataStore dataStore;

    @Test
    void testDataIsLoadedAtStartup() {
        Object latest = dataStore.get("latest_idr_rates");
        assertThat(latest).isNotNull();

        Object historical = dataStore.get("historical_idr_usd");
        assertThat(historical).isNotNull();

        Object currencies = dataStore.get("supported_currencies");
        assertThat(currencies).isNotNull();

        assertThat(latest).isInstanceOfAny(List.class);
        assertThat(historical).isInstanceOfAny(List.class);
        assertThat(currencies).isInstanceOfAny(List.class);
    }
}
