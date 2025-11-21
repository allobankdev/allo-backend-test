package test.allo.backend.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import test.allo.backend.service.IDRDataFetcher;
import test.allo.backend.storage.InMemoryStorage;
import static test.allo.backend.utils.ConstantUtils.HISTORICAL_IDR_USD;

import java.util.Iterator;

@Slf4j
@Service("historical_idr_usd")
@RequiredArgsConstructor
public class HistoricalIdrUsdImpl implements IDRDataFetcher {

    private final ObjectMapper mapper;
    private final InMemoryStorage storage;

    @Override
    public JsonNode fetchData() {

        Object storageData = storage.get(HISTORICAL_IDR_USD);
        JsonNode externalResponse = mapper.valueToTree(storageData);
        log.info("HistoricalIdrUsd data: {}", externalResponse);

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
