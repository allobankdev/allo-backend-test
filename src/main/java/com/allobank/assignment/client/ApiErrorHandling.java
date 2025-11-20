package com.allobank.assignment.client;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public interface ApiErrorHandling {
	void handleError(ClientHttpResponse response, HttpStatusCode statusCode) throws IOException;
}
