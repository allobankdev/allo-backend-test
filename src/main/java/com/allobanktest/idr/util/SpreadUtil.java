package com.allobanktest.idr.util;

import java.util.Locale;

public final class SpreadUtil {
    private SpreadUtil() {}

    public static double computeSpreadFactor(String githubUsername) {
        if (githubUsername == null) return 0.0;

        String u = githubUsername.toLowerCase(Locale.ROOT);
        int sum = 0;
        for (int i = 0; i < u.length(); i++) {
            sum += u.charAt(i);
        }
        int mod = sum % 1000;
        return mod / 100000.0;
    }
}
