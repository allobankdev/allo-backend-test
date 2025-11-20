package com.allo.idr;

import com.allo.idr.cache.ImmutableDataCache;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = IdrAggregatorApplication.class)
public class InitialDataLoaderTest {
    @Autowired
    private ImmutableDataCache cache;

    @Test
    void cachePopulatedStartup(){
        assertNotNull(cache.getAll());
    }
}
