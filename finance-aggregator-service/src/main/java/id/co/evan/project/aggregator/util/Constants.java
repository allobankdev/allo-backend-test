package id.co.evan.project.aggregator.util;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class Constants {

    public static final String CURRENCY = "currency";
    public static final String RATE = "rate";
    public static final String USD_BUYSPREAD_IDR = "USD_BuySpread_IDR";

    public static final String LATEST_IDR_RATES = "latest_idr_rates";
    public static final String HISTORICAL_IDR_USD = "historical_idr_usd";
    public static final String SUPPORTED_CURRENCIES = "supported_currencies";

    public static final String LATEST = "/latest";
    public static final String CURRENCIES = "/currencies";

    public static final List<String> RESOURCE_TYPES = List.of(
        LATEST_IDR_RATES,
        HISTORICAL_IDR_USD,
        SUPPORTED_CURRENCIES
    );
}
