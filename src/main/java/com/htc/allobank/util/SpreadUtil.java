package com.htc.allobank.util;

import com.htc.allobank.config.ExternalApiProperties;
import org.springframework.stereotype.Component;

@Component
public class SpreadUtil {
    private final ExternalApiProperties externalApiProperties;


    public SpreadUtil(ExternalApiProperties externalApiProperties) {
        this.externalApiProperties = externalApiProperties;
    }

    public double computeSpreadFactor() {
        int sum = 0;
        for (char c : externalApiProperties.getPersonalization().getGithubUsername().toCharArray()) {
            sum += (int) c;
        }
        int mod = sum % 1000;
        return mod / 100000.0;
    }

    public int computeAsciiSum() {
        int sum = 0;
        for (char c : externalApiProperties.getPersonalization().getGithubUsername().toCharArray()) {
            sum += (int) c;
        }
        return sum;
    }
}
