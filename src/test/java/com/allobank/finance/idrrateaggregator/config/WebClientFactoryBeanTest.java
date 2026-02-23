package com.allobank.finance.idrrateaggregator.config;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class WebClientFactoryBeanTest {

    @Test
    void shouldCreateWebClientWithBaseUrlFromProperties() {
        // given
        FrankfurterProperties properties = Mockito.mock(FrankfurterProperties.class);
        when(properties.getBaseUrl())
                .thenReturn("https://api.frankfurter.app");

        WebClientFactoryBean factoryBean =
                new WebClientFactoryBean(properties);

        // when
        WebClient webClient = factoryBean.getObject();

        // then
        assertThat(webClient).isNotNull();
        assertThat(factoryBean.getObjectType())
                .isEqualTo(WebClient.class);

        // verify interaction
        Mockito.verify(properties).getBaseUrl();
    }
}
