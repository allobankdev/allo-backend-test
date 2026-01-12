package id.co.evan.project.aggregator.config;

import id.co.evan.project.aggregator.config.properties.SecurityProperties;
import id.co.evan.project.aggregator.util.ErrorCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ApiKeyFilter implements WebFilter {

    private final SecurityProperties securityProperties;

    @Override
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        var apiKey = exchange.getRequest().getHeaders().getFirst("x-api-key");
        if (Objects.equals(securityProperties.apiKey(), apiKey)) return chain.filter(exchange);

        exchange.getResponse().setStatusCode(HttpStatus.CONFLICT);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        var errorFormat = String.format(
            "{\"errorCode\":\"%s\",\"errorMessage\":\"%s\",\"timestamp\":\"%s\",\"path\":\"%s\"}",
            ErrorCode.GENERAL_ERROR.getCode(),
            ErrorCode.GENERAL_ERROR.getDefaultMessage(),
            ZonedDateTime.now(),
            exchange.getRequest().getPath().value()
        );

        var responseWrapper = exchange.getResponse().bufferFactory().wrap(errorFormat.getBytes());

        return exchange.getResponse().writeWith(Mono.just(responseWrapper));
    }
}
