package com.example.idraggregator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "frankfurter")
public class FrankfurterProperties {
    private String baseUrl;
    private Timeouts timeouts = new Timeouts();

    public static class Timeouts {
        private long connectMillis = 2000;
        private long readMillis = 5000;
        public long getConnectMillis() { return connectMillis; }
        public void setConnectMillis(long connectMillis) { this.connectMillis = connectMillis; }
        public long getReadMillis() { return readMillis; }
        public void setReadMillis(long readMillis) { this.readMillis = readMillis; }
    }

    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
    public Timeouts getTimeouts() { return timeouts; }
    public void setTimeouts(Timeouts timeouts) { this.timeouts = timeouts; }
}

