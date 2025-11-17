package id.co.microservice.currency.currency_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CurrencyServiceApplicationTests {

	@Test
	void contextLoads() {
	}

    @Test
    void mainMethodRunsWithoutExceptions() {
        CurrencyServiceApplication.main(new String[]{});

        // Assert
        // If no exception is thrown, the test passes.
    }

}
