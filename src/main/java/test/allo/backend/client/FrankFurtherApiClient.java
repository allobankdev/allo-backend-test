package test.allo.backend.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@Service
@RequiredArgsConstructor
public class FrankFurtherApiClient {

    @Value("${external.frankfurter.endpoint.latest-idr-rates}")
    String uriLatestIdrRate;

    @Value("${external.frankfurter.endpoint.historical-idr-usd}")
    String uriHistoricalIdrUsd;

    @Value("${external.frankfurter.endpoint.supported-currencies}")
    String uriSupportedCurrency;

    private final WebClient webClient;
    private final ObjectMapper mapper;

    public JsonNode fetchLatestIdrRates() {
        return fetchData(uriLatestIdrRate, "latestIdrRate");
    }

    public JsonNode fetchHistoricalIdrUsd() {
        return fetchData(uriHistoricalIdrUsd, "historicalIdrUsd");
    }

    public JsonNode fetchSupportedCurrencies() {
        return fetchData(uriSupportedCurrency, "supportedCurrencies");
    }

    private JsonNode fetchData(String uri, String responseId) {
        try {
            JsonNode response = webClient.get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            log.info("{} response: {}", responseId, response);
            return response;
        } catch (WebClientResponseException e) {
            return failedResponse(e.getStatusCode().value(), "HTTP error", e.getResponseBodyAsString());
        } catch (WebClientRequestException e) {
            return failedResponse(503, "Service Unreachable", e.getMessage());
        } catch (WebClientException e) {
            return failedResponse(500, "Unexpected Error", e.getMessage());
        }
    }

    private ObjectNode failedResponse(int responseCode, String error, String message) {
        ObjectNode response = mapper.createObjectNode();
        response.put("status", responseCode);
        response.put("error", error);
        response.put("detail", message != null && !message.isEmpty() ? message : "Unknown");
        return response;
    }
}
