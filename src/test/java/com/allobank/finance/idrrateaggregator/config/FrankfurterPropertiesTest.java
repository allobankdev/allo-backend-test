package com.allobank.finance.idrrateaggregator.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.MapPropertySource;
import org.springframework.mock.env.MockEnvironment;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class FrankfurterPropertiesTest {

    @Test
    void shouldBindFrankfurterBaseUrlProperty() {
        MockEnvironment environment = new MockEnvironment();
        environment.getPropertySources().addFirst(
                new MapPropertySource("testProps", Map.of(
                        "frankfurter.base-url", "https://api.frankfurter.app"
                ))
        );

        Binder binder = Binder.get(environment);

        FrankfurterProperties properties =
                binder.bind("frankfurter", FrankfurterProperties.class)
                        .orElseThrow(null);

        assertThat(properties.getBaseUrl())
                .isEqualTo("https://api.frankfurter.app");
    }
}
