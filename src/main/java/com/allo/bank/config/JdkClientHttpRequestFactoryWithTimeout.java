package com.allo.bank.config;

import java.net.http.HttpClient;
import java.time.Duration;

import org.springframework.http.client.JdkClientHttpRequestFactory;

public class JdkClientHttpRequestFactoryWithTimeout extends JdkClientHttpRequestFactory {

    public JdkClientHttpRequestFactoryWithTimeout(HttpClient httpClient, Duration readTimeout) {
        super(httpClient);
        if (readTimeout != null) {
            setReadTimeout(readTimeout);
        }
    }
}
