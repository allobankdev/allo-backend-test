
package com.allo_backend_test.finance;

import com.allo_backend_test.finance.service.FinanceDataStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FinanceStartupRunnerTest {

    @Autowired
    private FinanceDataStore store;

    @Test
    void shouldLoadDataOnStartup() {
        assertFalse(store.getAll().isEmpty());
    }
}
