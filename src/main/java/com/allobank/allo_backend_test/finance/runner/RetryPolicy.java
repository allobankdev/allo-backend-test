package com.allobank.allo_backend_test.finance.runner;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RetryPolicy {

    private final int maxAttempts;
    private final long backoffMillis;

    public RetryPolicy(int maxAttempts, long backoffMillis) {
        this.maxAttempts = maxAttempts;
        this.backoffMillis = backoffMillis;
    }

    public boolean execute(String resourceType, Runnable task) {
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                task.run();
                return true;
            } catch (Exception e) {
                log.warn("attempt {}/{} failed for '{}': {}", attempt, maxAttempts, resourceType, e.getMessage());

                if (attempt < maxAttempts) {
                    try {
                        Thread.sleep(backoffMillis);
                    } catch (Exception exception) {
                        log.error("Error while sleep '{}': '{}'", resourceType, exception.getMessage());
                        return false;
                    }
                }
            }
        }
        return false;
    }
}