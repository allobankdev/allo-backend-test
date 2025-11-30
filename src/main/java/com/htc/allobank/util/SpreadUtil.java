package com.htc.allobank.util;

import com.htc.allobank.config.ExternalApiProperties;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@AllArgsConstructor
public class SpreadUtil {
    private ExternalApiProperties externalApiProperties;

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
