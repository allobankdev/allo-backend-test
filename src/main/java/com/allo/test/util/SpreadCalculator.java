package com.allo.test.util;

import org.springframework.stereotype.Component;

@Component
public class SpreadCalculator {

    private final double spread;

    public SpreadCalculator() {
        String username = "ramaaufadha"; 

        int sum = username.chars().sum();
        this.spread = (sum % 1000) / 100000.0;
    }

    public double getSpread() {
        return spread;
    }
}