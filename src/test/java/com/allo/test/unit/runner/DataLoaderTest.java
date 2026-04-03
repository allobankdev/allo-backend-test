package com.allo.test.unit.runner;

import com.allo.finance.runner.DataLoader;
import com.allo.finance.strategy.IDRDataFetcher;
import com.allo.finance.store.DataStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

class DataLoaderTest {

    @Mock DataStore store;
    @Mock IDRDataFetcher fetcher;

    @InjectMocks
    DataLoader loader;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldLoadDataIntoStore() throws Exception {

        when(fetcher.getType()).thenReturn("test");
        when(fetcher.fetch()).thenReturn(Map.of("key", "value"));

        loader = new DataLoader(List.of(fetcher), store);

        loader.run(null);

        verify(store).setAll(anyMap());
    }
}