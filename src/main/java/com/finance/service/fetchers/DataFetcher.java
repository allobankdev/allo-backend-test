package com.finance.service.fetchers;

import java.util.List;
import java.util.Map;

public interface DataFetcher {
    String resourceType();
    List<Map<String, Object>> fetch();
}
