package test.allo.backend.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import test.allo.backend.service.IDRDataFetcher;

import java.util.Iterator;

@Slf4j
@Service("historical_idr_usd")
@RequiredArgsConstructor
public class HistoricalIdrUsdImpl implements IDRDataFetcher {

    @Value("${external.frankfurter.endpoint.historical-idr-usd}")
    String uriHistoricalIdrUsd;

    private final ObjectMapper mapper;
    private final WebClient webClient;

    @Override
    public JsonNode fetchData() {

        JsonNode externalResponse = webClient.get()
                .uri(uriHistoricalIdrUsd)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
        log.info("external API response: {}", externalResponse);

        ArrayNode response = mapper.createArrayNode();
        if (externalResponse != null) {

            String baseCurrency = externalResponse.path("base").asText("");
            double baseAmount = externalResponse.path("amount").asDouble(0d);
            JsonNode rates = externalResponse.path("rates");

            Iterator<String> rateList = rates.fieldNames();
            while (rateList.hasNext()) {
                String date = rateList.next();
                JsonNode quoteRate = rates.get(date);

                ObjectNode node = mapper.createObjectNode();
                node.put("date", date);
                node.put(baseCurrency, baseAmount);
                node.setAll((ObjectNode) quoteRate);
                response.add(node);
            }
        }

        return response;
    }
}
