
package com.allo.test.util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SpreadUtil {
    @Value("${github.username}")
    private String username;

    public double calculateSpread() {
        int sum = username.chars().sum();
        return (sum % 1000) / 100000.0;
    }
}
