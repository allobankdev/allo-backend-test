package com.allobank.allobackend.core.fetcher.impl;

import com.allobank.allobackend.core.fetcher.IDRDataFetcher;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class SupportedCurrencyStrategy implements IDRDataFetcher {
    @Override
    public String getResourceType() { return "supported_currencies"; }

    @Override
    public JSONObject fetchData(RestClient client){
       String jsonResponse = client.get().uri("/currencies").retrieve().body(String.class);
        JSONObject output = new JSONObject();
        if (jsonResponse != null) {
            output.put("datas" , new JSONObject(jsonResponse));
        }

        return output;
    }
}
