package io.aditsukoco.allobank_test.models.enums;

import io.aditsukoco.allobank_test.exceptions.BadRequestRestException;

public enum ResourceTypeEnum {
    //latest_idr_rates, historical_idr_usd, or supported_currencies
    LatestIDRRates("latest_idr_rates"),
    HistoricalIDRUSD("historical_idr_usd"),
    SupportedCurrencies("supported_currencies");

    public final String label;

    ResourceTypeEnum(String label) {
        this.label = label;
    }

    public static ResourceTypeEnum stringToEnum(String label) throws BadRequestRestException {
        for (ResourceTypeEnum e : values()) {
            if (java.util.Objects.equals(e.label, label)) {
                return e;
            }
        }
        throw new BadRequestRestException("unknown value \"" + label + "\" for ResourceTypeEnum");
    }
}
