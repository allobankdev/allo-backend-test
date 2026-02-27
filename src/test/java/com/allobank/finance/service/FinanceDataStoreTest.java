package com.allobank.finance.service;

import com.allobank.finance.model.response.FinanceDataResponseBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class FinanceDataStoreTest {

    FinanceDataStore store;

    @BeforeEach
    void setUp() {
        store = new FinanceDataStore();
    }

    @Test
    void shouldReturnNullWhenStoreEmpty() {
        var result = store.get("latest");
        Assertions.assertNull(result);
    }

    @Test
    void shouldLoadAndRetrieveData() {
        var key = "latest";

        var response = FinanceDataResponseBuilder.builder()
                .resourceType(key)
                .fetchDate(ZonedDateTime.now())
                .data(List.of())
                .build();

        var data = Map.of(key, response);

        store.load(data);

        var result = store.get(key);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(key, result.resourceType());
    }

    @Test
    void shouldReturnAllData() {
        var response = FinanceDataResponseBuilder.builder()
                .resourceType("latest")
                .fetchDate(ZonedDateTime.now())
                .data(List.of())
                .build();

        store.load(Map.of("latest", response));

        var all = store.getAll();

        Assertions.assertEquals(1, all.size());
        Assertions.assertTrue(all.containsKey("latest"));
    }

    @Test
    void shouldBeImmutableAfterLoad() {
        var response = FinanceDataResponseBuilder.builder()
                        .resourceType("latest")
                        .fetchDate(ZonedDateTime.now())
                        .data(List.of())
                        .build();

        store.load(Map.of("latest", response));

        var all = store.getAll();

        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> all.put("new", response));
    }

    @Test
    void shouldOverrideDataOnReload() {
        var first = FinanceDataResponseBuilder.builder()
                        .resourceType("first")
                        .fetchDate(ZonedDateTime.now())
                        .data(List.of())
                        .build();

        var second = FinanceDataResponseBuilder.builder()
                        .resourceType("second")
                        .fetchDate(ZonedDateTime.now())
                        .data(List.of())
                        .build();

        store.load(Map.of("first", first));

        store.load(Map.of("second", second));

        Assertions.assertNull(store.get("first"));
        Assertions.assertNotNull(store.get("second"));
    }
}
