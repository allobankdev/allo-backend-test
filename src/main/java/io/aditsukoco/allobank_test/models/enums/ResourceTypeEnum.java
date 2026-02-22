package io.aditsukoco.allobank_test.models.enums;

import io.aditsukoco.allobank_test.exceptions.BadRequestRestException;

public enum ResourceTypeEnum {
    //latest_idr_rates, historical_idr_usd, or supported_currencies
    LatestIDRRates("latest_idr_rates"),
    HistoricalIDRUSD("historical_idr_usd"),
    SupportedCurrencies("supported_currencies");

    private String value;

    ResourceTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
