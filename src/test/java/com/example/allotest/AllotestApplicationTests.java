package com.example.allotest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.allotest.service.DataStoreService;

@SpringBootTest
class AllotestApplicationTests {

	@Autowired
	private DataStoreService store;

	@Test
	void contextLoads() {
		assertNotNull(store.get("latest_idr_rates"));
		assertNotNull(store.get("historical_idr_usd"));
		assertNotNull(store.get("supported_currencies"));
	}

}
