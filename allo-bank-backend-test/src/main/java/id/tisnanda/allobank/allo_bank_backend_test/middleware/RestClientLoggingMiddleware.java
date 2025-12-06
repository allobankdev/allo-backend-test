package id.tisnanda.allobank.allo_bank_backend_test.middleware;

import id.tisnanda.allobank.allo_bank_backend_test.config.AppStartupConfig;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;


@Component
public class RestClientLoggingMiddleware implements ClientHttpRequestInterceptor {

    private static final Logger log = Logger.getLogger(RestClientLoggingMiddleware.class);

    @Autowired
    private AppStartupConfig startupConfig;

    private static final List<String> SENSITIVE_HEADERS = List.of(
            "apiKey", "authorization", "x-api-key", "x-consumer-custom-id"
    );

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        try {

            if (startupConfig.isStartupMode()) {
                return execution.execute(request, body);
            }

            logRequest(request, body);

            ClientHttpResponse response = execution.execute(request, body);

            logResponse(request, response);

            return response;
        } catch (IOException e) {
            log.errorf(e, "HTTP request failed: %s %s", request.getMethod(), request.getURI());
            throw e;
        }
    }

    private void logRequest(HttpRequest request, byte[] body) {
        log.infof(
                "REST-REQ %s %s headers=%s body=%s",
                request.getMethod(),
                request.getURI(),
                maskHeaders(request.getHeaders()),
                new String(body, StandardCharsets.UTF_8)
        );
    }

    private void logResponse(HttpRequest request, ClientHttpResponse response) throws IOException {
        byte[] body = readResponseBody(response);
        log.infof(
                "REST-RES %s %s -> %d headers=%s body=%s",
                request.getMethod(),
                request.getURI(),
                response.getStatusCode().value(),
                maskHeaders(response.getHeaders()),
                new String(body, StandardCharsets.UTF_8)
        );
    }

    private byte[] readResponseBody(ClientHttpResponse response) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            response.getBody().transferTo(baos);
            return baos.toByteArray();
        }
    }

    private String maskHeaders(HttpHeaders headers) {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            if (!first) sb.append(", ");
            first = false;

            String key = entry.getKey();
            String value = entry.getValue().toString();

            if (SENSITIVE_HEADERS.stream().anyMatch(h -> h.equalsIgnoreCase(key))) {
                value = "[REDACTED]";
            }

            sb.append(key).append("=").append(value);
        }
        sb.append("}");
        return sb.toString();
    }


}
