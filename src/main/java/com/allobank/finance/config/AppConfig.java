package com.allobank.finance.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Konfigurasi Spring yang mendaftarkan {@link WebClientFactoryBean} sebagai
 * bean.
 * Spring secara otomatis memanggil {@link WebClientFactoryBean#getObject()}
 * sehingga bean bertipe WebClient tersedia untuk injection di seluruh aplikasi.
 */
@Configuration
public class AppConfig {

    private final FrankfurterProperties properties;

    public AppConfig(FrankfurterProperties properties) {
        this.properties = properties;
    }

    /**
     * Mendaftarkan FactoryBean – Spring akan menggunakannya untuk menghasilkan
     * instance {@link WebClient}. Sesuai Constraint B: pembuatan client dilakukan
     * melalui FactoryBean, bukan @Bean langsung yang mengembalikan WebClient.
     */
    @Bean
    public WebClientFactoryBean frankfurterWebClientFactory() {
        return new WebClientFactoryBean(properties);
    }

    /**
     * Bean WebClient yang dihasilkan dari FactoryBean.
     * Nama bean "frankfurterWebClient" digunakan agar tidak konflik
     * dengan WebClient bean lain yang mungkin ada.
     */
    @Bean(name = "frankfurterWebClient")
    public WebClient frankfurterWebClient(WebClientFactoryBean factory) throws Exception {
        return factory.getObject();
    }
}
