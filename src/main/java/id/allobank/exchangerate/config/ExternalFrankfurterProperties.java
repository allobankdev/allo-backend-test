package id.allobank.exchangerate.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "external.frankfurter")
public class ExternalFrankfurterProperties {

    private String baseUrl;

    private Timeout timeout = new Timeout();

    @Data
    public static class Timeout {
        private int connectMs;
        private int responseMs;
        private int readSec;
        private int writeSec;
    }

    }
