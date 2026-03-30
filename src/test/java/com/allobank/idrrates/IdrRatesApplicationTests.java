package com.allobank.idrrates;

import com.allobank.idrrates.store.DataStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class IdrRatesApplicationTests {

	@Autowired
	private DataStore dataStore;

	@Test
	void contextLoads() {
	}

	@Test
	void dataStore_shouldBeInitializedWithAllThreeResources() {
		assertThat(dataStore.get("latest_idr_rates")).isNotNull();
		assertThat(dataStore.get("historical_idr_usd")).isNotNull();
		assertThat(dataStore.get("supported_currencies")).isNotNull();
	}
}
