package co.id.allobank.finance.exception;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "handling.error")
public record ErrorHandlingProperties(
        String prefix,
        int serviceCode,
        Common common
) {
    public record Common(
            String prefix,
            int serviceCode,
            Map<String, Mapping> mappings
    ) {}

    public record Mapping(
            String status,
            String errorDesc,
            int errorCode
    ) {}
}
