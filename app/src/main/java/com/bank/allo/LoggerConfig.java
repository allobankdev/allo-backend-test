package com.bank.allo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import java.util.Optional;

@Configuration
public class LoggerConfig {

    @Bean
    @Scope("prototype")
    public Logger logger(final InjectionPoint injectionPoint) {
        Class<?> clazz = Optional.ofNullable(injectionPoint.getMethodParameter())
                .map(methodParam -> methodParam.getContainingClass())
                .orElse(null);

        if (clazz == null) {
            clazz = Optional.ofNullable(injectionPoint.getField())
                    .map(field -> field.getDeclaringClass())
                    .orElse(null);
        }

        if (clazz == null) {
            clazz = Object.class;
        }

        return LoggerFactory.getLogger(clazz);
    }

}
