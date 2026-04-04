package com.allobank.allobank_api.dto.oas;

import java.util.Map;

public class HistoricalOas {

    public static class Response {
        private Map<String, Map<String, Double>> rates;

        public Response(Map<String, Map<String, Double>> rates) {
            this.rates = rates;
        }

        public Map<String, Map<String, Double>> getRates() {
            return rates;
        }

    }
    
}
