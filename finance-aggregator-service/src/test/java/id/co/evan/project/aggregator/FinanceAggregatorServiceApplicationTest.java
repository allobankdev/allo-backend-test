package id.co.evan.project.aggregator;

import id.co.evan.project.aggregator.service.DataStoreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
class FinanceAggregatorServiceApplicationTests {

    @Autowired
    private DataStoreService dataStoreService;

    @Test
    void shouldInitializeDataOnStartup() {
        var latestDataMono = dataStoreService.getData("latest_idr_rates");

        var response = latestDataMono.block();

        assertNotNull(response, "Data should be initialized by ApplicationRunner");
        assertNotNull(response.data(), "Data list should not be null");
        assertFalse(response.data().isEmpty(), "Stored data should not be empty");
    }
}