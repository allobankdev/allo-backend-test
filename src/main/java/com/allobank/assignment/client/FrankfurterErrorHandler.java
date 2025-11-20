package com.allobank.assignment.client;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FrankfurterErrorHandler implements ResponseErrorHandler, ApiErrorHandling {

	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		return response.getStatusCode().isError();
	}

	@SuppressWarnings("removal")
	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		throwException(response);
	}

	@Override
	public void handleError(ClientHttpResponse response, HttpStatusCode statusCode) throws IOException {
		throwException(response);
	}

	private void throwException(ClientHttpResponse response) throws IOException {
		String body = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
		throw new RestClientException("Frankfurter API error [" + response.getStatusCode() + "]: " + body);
	}
}
