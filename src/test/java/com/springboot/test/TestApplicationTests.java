package com.springboot.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.test.config.CurrencyApiConfig;
import com.springboot.test.dto.LatestRateDTO;
import com.springboot.test.service.ApiService;
import com.springboot.test.service.IDRDataFetcherService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest
class TestApplicationTests {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private IDRDataFetcherService service;

	private MockRestServiceServer mockServer;
	private ObjectMapper mapper = new ObjectMapper();

	@BeforeEach
	public void setUp() {
		mockServer = MockRestServiceServer.createServer(restTemplate);
	}

	@Test
	void testExternalCall() throws JsonProcessingException, URISyntaxException {
		Map<String, BigDecimal> rates = new HashMap<>();
		rates.put("USD", new BigDecimal(0.000059));
		LatestRateDTO dto = new LatestRateDTO(1.0, "IDR", "2026-03-05", rates, new BigDecimal(1));
		mockServer.expect(ExpectedCount.once(),
				requestTo(new URI("https://api.frankfurter.app/latest?base=IDR")))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withStatus(HttpStatus.OK)
						.contentType(MediaType.APPLICATION_JSON)
						.body(mapper.writeValueAsString(dto))
				);
		LatestRateDTO latestDto = service.getLatestIdrRate();
		mockServer.verify();
		Assertions.assertEquals(dto.getAmount(), latestDto.getAmount());
	}

}
