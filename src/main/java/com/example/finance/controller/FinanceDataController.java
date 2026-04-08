package com.example.finance.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.finance.service.FinanceDataService;

@RestController
@RequestMapping("/api/finance")
public class FinanceDataController {

	private static final Logger log = LoggerFactory.getLogger(FinanceDataController.class);

	private final FinanceDataService financeDataService;

	public FinanceDataController(FinanceDataService financeDataService) {
		this.financeDataService = financeDataService;
	}

	@GetMapping("/data/{resourceType}")
	public Object getData(@PathVariable String resourceType) {
		log.info("Incoming request for resourceType={}", resourceType);

		try {

			Object result = financeDataService.getData(resourceType);

			log.info("Response={}", result);

			return result;

		} catch (IllegalArgumentException e) {
			log.error("Invalid resourceType={}", resourceType, e);
			return "Invalid resourceType";

		} catch (Exception e) {
			log.error("Error processing request ", e);
			return "Error processing request";
		}

	}
}
