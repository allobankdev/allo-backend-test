package io.aditsukoco.allobank_test.repositories.spreadFactor;

import org.springframework.beans.factory.annotation.Value;

public class SpreadFactorDataRepositoryImpl implements SpreadFactorDataRepositoryInterface {

    private final double spreadFactor;

    public SpreadFactorDataRepositoryImpl(@Value("${github.username}") String githubUsername) {
        int unicodeSum = 0;
        for (char a : githubUsername.toCharArray()) {
            unicodeSum += a;
        }

        // Spread Factor = (Sum of Unicode Values % 1000) / 100000.0 (This will yield a unique factor between 0.00000 and 0.00999, ensuring a personalized result.)
        this.spreadFactor = (unicodeSum % 1000) / 100000.0;
    }

    @Override
    public double getSpreadFactor() {
        return this.spreadFactor;
    }
}
