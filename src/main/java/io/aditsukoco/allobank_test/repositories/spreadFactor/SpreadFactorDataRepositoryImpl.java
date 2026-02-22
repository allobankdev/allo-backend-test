package io.aditsukoco.allobank_test.repositories.spreadFactor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
public class SpreadFactorDataRepositoryImpl implements SpreadFactorDataRepositoryInterface {

    private final double spreadFactor;

    public SpreadFactorDataRepositoryImpl(@Value("${github.username}") String githubUsername) {
        int unicodeSum = 0;
        for (char a : githubUsername.toCharArray()) {
            unicodeSum += a;
        }
        log.info("Unicode sum is " + unicodeSum);

        // Spread Factor = (Sum of Unicode Values % 1000) / 100000.0 (This will yield a unique factor between 0.00000 and 0.00999, ensuring a personalized result.)
        this.spreadFactor = (unicodeSum % 1000) / 100000.0;
        log.info("Spread factor is " + this.spreadFactor);
    }

    @Override
    public double getSpreadFactor() {
        return this.spreadFactor;
    }
}
