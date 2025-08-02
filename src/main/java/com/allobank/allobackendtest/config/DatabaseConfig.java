package com.allobank.allobackendtest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.allobank.allobackendtest.repository")
@EnableTransactionManagement
public class DatabaseConfig {
    // Additional database configurations can be added here if needed
}