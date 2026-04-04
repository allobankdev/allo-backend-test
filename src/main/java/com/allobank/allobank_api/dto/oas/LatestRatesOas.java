package com.allobank.allobank_api.dto.oas;

import java.util.Map;

public class LatestRatesOas {

    public static class Request {

    }

    public static class Response {

        private String base;
        private Map<String, Double> rates;
        private Double usdBuySpreadIdr;

        public Response(String base, Map<String, Double> rates, Double usdBuySpreadIdr) {
            this.base = base;
            this.rates = rates;
            this.usdBuySpreadIdr = usdBuySpreadIdr;
        }

        public String getBase() { return base; }
        public Map<String, Double> getRates() { return rates; }
        public Double getUsdBuySpreadIdr() { return usdBuySpreadIdr; }
    }
    
}
