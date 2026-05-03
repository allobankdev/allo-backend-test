package cory.sakti.Financial.strategy;

import org.springframework.web.client.RestTemplate;

public interface FinancialDataStrategy {
    String getResourceType();
    Object fetchAndTransform(RestTemplate restTemplate);
}
