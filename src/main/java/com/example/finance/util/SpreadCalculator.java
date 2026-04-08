package com.example.finance.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpreadCalculator {
	
	private static final Logger log = LoggerFactory.getLogger(SpreadCalculator.class);


	private SpreadCalculator() {
	}

	public static double calculateSpreadFactor(String username) {

		int sum = username.chars().sum();

		return (sum % 1000) / 100000.0;
	}

	public static double calculateUsdBuySpread(double rateUsd, String username) {

		double spreadFactor = calculateSpreadFactor(username);
		log.info("Spread Factor : " + spreadFactor);

		double result = (1 / rateUsd) * (1 + spreadFactor);

		return BigDecimal.valueOf(result).setScale(5, RoundingMode.HALF_UP) // 5 digit decimal
				.doubleValue();
	}
}
