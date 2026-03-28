package com.allobank.allo_backend_test.finance;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(MockDataSourceClient.class)
class FinanceApplicationTests {

	@Test
	void contextLoads() {}
}