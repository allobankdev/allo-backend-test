package com.hend.backend;

import com.hend.backend.service.FinanceDataStorage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;

import static org.junit.jupiter.api.Assertions.assertNotNull;


/**
 * @author : hend wunga
 */


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
		// Port 0 berarti acak, ${wiremock.server.port} akan mengambil port tersebut
		"app.frankfurter.base-url=http://localhost:${wiremock.server.port}"
})
@AutoConfigureWireMock(port = 0)
class BackendApplicationTests {

	@Autowired
	private FinanceDataStorage storage;

	@Test
	void contextLoads() {
	}

	@Test
	void testDataInjectedOnStartup() {
		// Karena kita menggunakan file di /mappings, data pasti sudah terisi
		// saat ApplicationRunner berjalan di awal.
		assertNotNull(storage.getData("latest_idr_rates"), "Data Latest IDR tidak boleh null");
		assertNotNull(storage.getData("historical_idr_usd"), "Data Historical tidak boleh null");
		assertNotNull(storage.getData("supported_currencies"), "Data Currencies tidak boleh null");
	}
}