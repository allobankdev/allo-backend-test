package com.allobank.idr_rate_aggregator.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


/**
 * FactoryBean untuk membuat instance WebClient yang akan digunakan
 * untuk memanggil Frankfurter API.
 * 
 * FactoryBean digunakan untuk memberikan kontrol lebih pada pembuatan bean,
 * termasuk konfigurasi yang complex dan externalisasi properti.
 * 
 * Constraint B: Client Factory Bean - WAJIB menggunakan FactoryBean
 */
@Component
public class WebClientFactoryBean implements FactoryBean<WebClient> {

    @Value("${frankfurter.api.base-url}")
    private String baseUrl;

    @Value("${frankfurter.api.timeout:5000}")
    private int timeout;

    /**
     * Method ini dipanggil Spring untuk membuat instance WebClient
     */
    @Override
    public WebClient getObject() throws Exception {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Accept", "application/json")
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024)) // 16MB buffer
                .build();
    }

    /**
     * Menentukan tipe object yang dibuat oleh factory ini
     */
    @Override
    public Class<?> getObjectType() {
        return WebClient.class;
    }

    /**
     * WebClient adalah singleton (satu instance untuk seluruh aplikasi)
     */
    @Override
    public boolean isSingleton() {
        return true;
    }
}