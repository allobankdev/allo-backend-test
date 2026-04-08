package com.example.finance.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "frankfurter")
public class FrankfurterProperties {

	private String baseUrl;
	private Endpoints endpoints;

	public static class Endpoints {
		private String currencies;
		private String latest;
		private String historical;

		public String getCurrencies() {
			return currencies;
		}

		public void setCurrencies(String currencies) {
			this.currencies = currencies;
		}

		public String getLatest() {
			return latest;
		}

		public void setLatest(String latest) {
			this.latest = latest;
		}

		public String getHistorical() {
			return historical;
		}

		public void setHistorical(String historical) {
			this.historical = historical;
		}
	}
}