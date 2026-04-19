package com.allobankdev.exchangrate.config;

import com.allobankdev.exchangrate.execption.ApiClientException;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class ResTemplateErrorHandler implements ResponseErrorHandler{

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().is4xxClientError()
                || response.getStatusCode().is5xxServerError();
    }

    @Override
    public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
        String body = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
        String message = String.format("External API Client error: %s %s", method.name(), url.toString());

        throw new ApiClientException(
                response.getStatusCode().value(),
                body,
                message
        );
    }
}
