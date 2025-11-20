package com.allo.idr.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SpreadCalculator {

    private final String usernameGithub;

    public SpreadCalculator(@Value("${app.github-username}")String usernameGithub) {
        this.usernameGithub = usernameGithub;
    }

    public double calculate() {
        int sum = 0;
        for (char cek : usernameGithub.toLowerCase().toCharArray()){
            sum += (int) cek;
        }
        return (sum % 1000) / 100000.0;
    }
}
