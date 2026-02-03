package com.example.allobank.config;

import java.math.BigDecimal;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "exchange.spread")
public class SpreadProperties {
	private BigDecimal factor;
}
