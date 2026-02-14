package cory.sakti.Financial.service;

import cory.sakti.Financial.strategy.AbstractFinancialStrategy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;

@Service("latest_idr_rates")
public class LatestIDRRateService extends AbstractFinancialStrategy {

    private final String githubUsername;

    public LatestIDRRateService(@Value("${app.github.username}") String githubUsername) {
        this.githubUsername = githubUsername;
    }

    @Override
    public String getResourceType() { return "latest_idr_rates"; }

    @Override
    protected String getUri() { return "/latest?base=IDR"; }

    @Override
    protected Object transform(JsonNode node) {
        return new HashMap<>();
    }
}
