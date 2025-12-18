package com.allo.backendtest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BackendtestApplicationTests {

	@Test
	void contextLoads() {
		String text = "MrPandoyo".toLowerCase();
		text.chars()
				.forEach(System.out::println);
		System.out.println("BackendtestApplicationTests.contextLoads SUM : " + text.chars().sum());
	}

}
