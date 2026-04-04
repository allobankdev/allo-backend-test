package com.allobank.allobank_api.dto.oas;

import java.util.Map;

public class CurrencyOas {
    
    public static class Response {
        private Map<String, String> currencies;

        public Response(Map<String, String> currencies) {
            this.currencies = currencies;
        }

        public Map<String, String> getCurrencies() {
            return currencies;
        }
    }
}
