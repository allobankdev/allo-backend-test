package com.example.idraggregator.util;

/**
 * Compute unique Spread Factor from GitHub username (lowercase).
 *
 * Spread Factor = (sum of Unicode values % 1000) / 100000.0
 */
public final class SpreadUtil {
    private SpreadUtil() {}

    public static double computeSpreadFactorFor(String githubUsernameLowercase) {
        if (githubUsernameLowercase == null) githubUsernameLowercase = "";
        int sum = 0;
        for (char c : githubUsernameLowercase.toCharArray()) {
            sum += (int) c;
        }
        int mod = sum % 1000;
        return mod / 100000.0;
    }
}
