package com.allobank.exercise.api.boot;

import com.allobank.exercise.api.cache.ResourceCache;
import com.allobank.exercise.api.enumeration.ResourceType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ResourceCacheTest {

    @Autowired
    ResourceCache resourceCache;

    @Test
    void testCacheLoadedOnApplicationRunner() {
        assertTrue( resourceCache.isReady(), "Cache must be loaded by ApplicationRunner during startup" );
        assertEquals(3, resourceCache.getAllCache().size(), "data cache should contains 3 client's response API");
    }

    @Test
    void testImmutableCacheAfterInitialized() {
        assertThrows(UnsupportedOperationException.class, ()-> resourceCache.getAllCache().put("foo", "bar"));
    }
}
