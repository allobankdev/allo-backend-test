package com.allobank.allobackend.core.fetcher.impl;

import com.allobank.allobackend.common.util.Utils;
import com.allobank.allobackend.core.fetcher.IDRDataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class LatestIdrStrategy implements IDRDataFetcher {
    @Value("${app.usernamegithub}")
    public String usernameGithub;

    @Override
    public String getResourceType(){
        return "latest_idr_rates";
    }

    @Override
    public JSONObject fetchData(RestClient client){
        String jsonString = client.get().uri("/latest?base=IDR").retrieve().body(String.class);
        JSONObject ouput = new JSONObject();
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONObject rate = new JSONObject();
        if(jsonObject.has("rates")){
            JSONObject rates = jsonObject.getJSONObject("rates");
            for (String currency : rates.keySet()){
                double rateVal = rates.getDouble(currency);

                double factor = Utils.caculateSpreadFactorByUsername(usernameGithub);
                double buySpread = Utils.calculateBuySpread(rateVal , factor);

                rate.put(currency+"_BuySpread_IDR", buySpread);

            }
            ouput.put("datas" , rate);

        }
        return ouput;
    }
}
