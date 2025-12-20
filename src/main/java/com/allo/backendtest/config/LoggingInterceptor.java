package com.allo.backendtest.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class LoggingInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException, IOException {
        log.info("Request: {} {}", request.getMethod(), request.getURI());
        var response = execution.execute(request, body);
        log.info("Response Status: {}", response.getStatusCode());
        log.info("Response Body: {}",  new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8));
        return response;
    }
}