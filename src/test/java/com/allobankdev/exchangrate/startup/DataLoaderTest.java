package com.allobankdev.exchangrate.startup;

import com.allobankdev.exchangrate.client.ApiClient;
import com.allobankdev.exchangrate.constant.ResourceType;
import com.allobankdev.exchangrate.dto.CurrencyResponse;
import com.allobankdev.exchangrate.service.factory.DataFetcherFactory;
import com.allobankdev.exchangrate.service.store.DataStore;
import com.allobankdev.exchangrate.service.strategy.impl.CurrencyFetcher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.DefaultApplicationArguments;

import java.util.Collections;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataLoaderTest {

    @Mock
    private ApiClient client;

    @Mock
    private DataFetcherFactory factory;

    @Mock
    private DataStore store;

    @InjectMocks
    private DataLoader loader;

    @Test
    public void testDataLoader_Valid() {
        Set<ResourceType> types = Set.of(ResourceType.SUPPORTED_CURRENCIES);
        CurrencyResponse mockResponse = new CurrencyResponse();
        mockResponse.put("USD", "United States Dollar");

        when(factory.getAllTypes()).thenReturn(types);
        when(factory.get(any())).thenReturn(new CurrencyFetcher(client));
        when(client.getCurrencies()).thenReturn(mockResponse);
        doNothing().when(store).save(any(), any());

        loader.run(new DefaultApplicationArguments("test"));

        verify(store, times(1)).save(any(), any());
    }

    @Test
    public void testDataLoader_Invalid() {
        when(factory.getAllTypes()).thenReturn(Collections.emptySet());

        loader.run(new DefaultApplicationArguments("test"));

        verify(factory, times(0)).get(any());
        verify(store, times(0)).save(any(), any());
    }
}
