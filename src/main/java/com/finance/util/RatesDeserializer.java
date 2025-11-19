package com.finance.util;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.finance.dto.Rates;
import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RatesDeserializer extends JsonDeserializer<Rates> {
    @Override
    public Rates deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        Map<String, Double> map = new HashMap<>();

        JsonNode node = p.getCodec().readTree(p);
        Iterator<String> fieldNames = node.fieldNames();

        while (fieldNames.hasNext()) {
            String field = fieldNames.next();
            map.put(field, node.get(field).asDouble());
        }

        return new Rates(map);
    }
}

