package com.allo.backendtest.dto.frankfurter;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CurrenciesDto(String message, @JsonAnyGetter Map<String,String> mapCurrencies) { }
