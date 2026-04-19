package com.allobankdev.exchangrate.util;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

@Slf4j
public class RetryUtil {
    public static <T> T executeWithRetry(Supplier<T> action, int maxRetries, long delayMillis) {
        int retries = 0;

        while (true) {
            try {
                return action.get();
            } catch (Exception e) {
                retries++;
                if (retries >= maxRetries) {
                    throw new RuntimeException("Max retries reached. Last error: " + e.getMessage(), e);
                }

                log.warn("Retrying... Attempt {}/{}. Error: {}", retries, maxRetries, e.getMessage());

                try {
                    Thread.sleep(delayMillis);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Retry interrupted", ie);
                }
            }
        }
    }
}
