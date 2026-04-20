package com.allobank.backendtest.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApiConstants {
    public static final String LATEST_PATH = "/latest";
    public static final String CURRENCIES_PATH = "/currencies";
    public static final String FROM_PARAM = "from";
    public static final String TO_PARAM = "to";
    public static final String BASE_PARAM = "base";
    public static final String SYMBOLS_PARAM = "symbols";
    public static final String AMOUNT_PARAM = "amount";
}
