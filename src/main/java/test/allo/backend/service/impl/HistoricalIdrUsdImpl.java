package test.allo.backend.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import test.allo.backend.service.IDRDataFetcher;

@Service("historical_idr_usd")
@RequiredArgsConstructor
public class HistoricalIdrUsdImpl implements IDRDataFetcher {

    private final ObjectMapper mapper;

    @Override
    public JsonNode fetchData() {
        ObjectNode node = mapper.createObjectNode();
        node.put("name", "historical_idr_usd");
        return node;
    }
}
