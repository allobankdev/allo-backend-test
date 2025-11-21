package test.allo.backend.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import test.allo.backend.service.IDRDataFetcher;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

            Iterator<String> ratesValue = rates.fieldNames();
            while (ratesValue.hasNext()) {
                ObjectNode node = mapper.createObjectNode();
                String date = ratesValue.next();
                JsonNode rateData = rates.get(date);
                String quoteCurrency = rateData.fieldNames().next();
                double quoteAmount = rateData.path(quoteCurrency).asDouble(0d);

                node.put("date", date);
                node.put(baseCurrency, baseAmount);
                node.put(quoteCurrency, quoteAmount);

                response.add(node);
            }
        }

        return response;
    }
}
