package com.mlutfiazizan13.allo_backend_test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class AlloBackendTestApplicationTests {

	@MockitoBean
	private RestTemplate restTemplate;

	@Test
	void contextLoads() {
	}

}
