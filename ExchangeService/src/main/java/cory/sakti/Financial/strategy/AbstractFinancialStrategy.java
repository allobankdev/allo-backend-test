package cory.sakti.Financial.strategy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;

@Slf4j
public abstract class AbstractFinancialStrategy implements FinancialDataStrategy {
    @Override
    public Object fetchAndTransform(RestTemplate restTemplate) {
        JsonNode response = restTemplate.getForObject(getUri(), JsonNode.class);
        return transform(response);
    }

    protected abstract String getUri();

    protected abstract Object transform(JsonNode node);
}
