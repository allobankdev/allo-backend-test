package com.allobank.splitbill.service;

import java.util.Locale;
import java.util.regex.Pattern;

final class UsernamePolicy {
    private static final Pattern USERNAME = Pattern.compile("[a-z0-9_]{3,30}");

    private UsernamePolicy() {}

    static String normalize(String value) {
        if (value == null) {
            throw new IllegalArgumentException("username is required");
        }
        String normalized = value.trim().toLowerCase(Locale.ROOT);
        if (normalized.startsWith("@")) {
            normalized = normalized.substring(1);
        }
        if (!USERNAME.matcher(normalized).matches()) {
            throw new IllegalArgumentException(
                    "username must contain 3 to 30 lowercase letters, numbers, or underscores");
        }
        return normalized;
    }

    static String handle(String normalizedUsername) {
        return "@" + normalizedUsername;
    }
}
