package com.example.idrapi.strategy;

import java.util.List;
import java.util.Map;



public interface IDRDataFetcher {

    String getResourceType();

    List<Map<String, Object>> fetch();
}
