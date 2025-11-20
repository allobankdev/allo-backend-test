package test.allo.backend.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import test.allo.backend.service.IDRDataFetcher;

@Service("supported_currencies")
@RequiredArgsConstructor
public class SupportedCurrenciesImpl implements IDRDataFetcher {

    private final ObjectMapper mapper;

    @Override
    public JsonNode fetchData() {
        ObjectNode node = mapper.createObjectNode();
        node.put("name", "supported_currencies");
        return node;
    }
}
