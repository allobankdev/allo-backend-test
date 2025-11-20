package com.allobank.assignment.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "frankfurter-api")
public class CurrencyApiProperties {

	private String baseUrl;
	private String latestEndpoint;
	private String historicalEndpoint;
	private String currenciesEndpoint;
	private String githubUsername;

	private int connectTimeoutMs = 2000;
	private int readTimeoutMs = 5000;

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getLatestEndpoint() {
		return latestEndpoint;
	}

	public void setLatestEndpoint(String latestEndpoint) {
		this.latestEndpoint = latestEndpoint;
	}

	public String getHistoricalEndpoint() {
		return historicalEndpoint;
	}

	public void setHistoricalEndpoint(String historicalEndpoint) {
		this.historicalEndpoint = historicalEndpoint;
	}

	public String getCurrenciesEndpoint() {
		return currenciesEndpoint;
	}

	public void setCurrenciesEndpoint(String currenciesEndpoint) {
		this.currenciesEndpoint = currenciesEndpoint;
	}

	public int getConnectTimeoutMs() {
		return connectTimeoutMs;
	}

	public void setConnectTimeoutMs(int connectTimeoutMs) {
		this.connectTimeoutMs = connectTimeoutMs;
	}

	public int getReadTimeoutMs() {
		return readTimeoutMs;
	}

	public void setReadTimeoutMs(int readTimeoutMs) {
		this.readTimeoutMs = readTimeoutMs;
	}

	public String getGithubUsername() {
		return githubUsername;
	}

	public void setGithubUsername(String githubUsername) {
		this.githubUsername = githubUsername;
	}
}
