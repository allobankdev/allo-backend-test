package com.allobank.allobackend.core.fetcher;

import org.json.JSONObject;
import org.springframework.web.client.RestClient;

public interface IDRDataFetcher {
    String getResourceType();
    JSONObject fetchData(RestClient client);
}
