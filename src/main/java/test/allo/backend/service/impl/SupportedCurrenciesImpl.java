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
@Service("supported_currencies")
@RequiredArgsConstructor
public class SupportedCurrenciesImpl implements IDRDataFetcher {

    @Value("${external.frankfurter.endpoint.supported-currencies}")
    String uriSupportedCurrency;

    private final ObjectMapper mapper;
    private final WebClient webClient;

    @Override
    public JsonNode fetchData() {

        JsonNode externalResponse = webClient.get()
                .uri(uriSupportedCurrency)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
        log.info("external API response: {}", externalResponse);

        ArrayNode response = mapper.createArrayNode();
        if (externalResponse != null) {

            Iterator<String> currencyList = externalResponse.fieldNames();
            while (currencyList.hasNext()) {
                String currencyCode = currencyList.next();
                String currencyName = externalResponse.path(currencyCode).asText("");

                ObjectNode node = mapper.createObjectNode();
                node.put("code", currencyCode);
                node.put("name", currencyName);
                response.add(node);
            }
        }

        return response;
    }
}
