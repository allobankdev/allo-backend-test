package com.backend.allobank.dto;

import java.util.Map;

public record FrankfurterRatesResponse (

        String base,
        String date,
        Map<String, Object> rates

){}


