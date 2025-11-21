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

import java.util.Iterator;

import static test.allo.backend.utils.ConstantUtils.SUPPORTED_CURRENCIES;

@Slf4j
@Service("supported_currencies")
@RequiredArgsConstructor
public class SupportedCurrenciesImpl implements IDRDataFetcher {

    private final ObjectMapper mapper;
    private final InMemoryStorage storage;

    @Override
    public JsonNode fetchData() {

        Object storageData = storage.get(SUPPORTED_CURRENCIES);
        JsonNode externalResponse = mapper.valueToTree(storageData);
        log.info("supportedCurrencies data: {}", externalResponse);

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
