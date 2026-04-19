package com.allobank.allobackend.core.fetcher.impl;

import com.allobank.allobackend.core.fetcher.IDRDataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;

@Slf4j
@Component
public class HistoricalIdrStrategy implements IDRDataFetcher {

    @Override
    public String getResourceType() { return "historical_idr_usd"; }

    @Override
    public JSONObject fetchData(RestClient client){

        LocalDate endDate  = LocalDate.now();
        LocalDate startDate = endDate.minusDays(5);

        String dateRange = startDate+".."+endDate;

        log.info("Date range="+dateRange);

        String jsonResponse = client.get().uri(
                uriBuilder -> uriBuilder.path("/"+dateRange).
                        queryParam("from" , "IDR")
                        .build()).retrieve().body(String.class);
        JSONObject output = new JSONObject();


        if(jsonResponse != null){
            output.put("datas", new JSONObject(jsonResponse));
        }

        return  output;


    }
}
