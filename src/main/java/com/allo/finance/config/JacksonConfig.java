package com.allo.finance.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;

@Configuration
public class JacksonConfig {

    @SuppressWarnings("deprecation")
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> builder.featuresToEnable(
                SerializationFeature.WRITE_BIGDECIMAL_AS_PLAIN
        );
    }
}