package com.allobank.backendtest.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessageConstants {
    public static final String ERROR_FETCH_LATEST = "Failed to fetch latest IDR rates";
    public static final String ERROR_FETCH_HISTORICAL = "Failed to fetch historical IDR/USD data";
    public static final String ERROR_FETCH_CURRENCIES = "Failed to fetch supported currencies";
    public static final String ERROR_EXTERNAL_API = "External API error";
    public static final String ERROR_RESOURCE_NOT_FOUND = "Unknown resource type: ";
    public static final String ERROR_DATA_NOT_READY = "Data store has not been initialized. Please try again later.";
    public static final String ERROR_NULL_RESPONSE = "Received null or empty response from Frankfurter API";
    
    public static final String ERR_TYPE_NOT_FOUND = "Not Found";
    public static final String ERR_TYPE_SERVICE_UNAVAILABLE = "Service Unavailable";
    public static final String ERR_TYPE_VALIDATION = "Validation Error";
    public static final String ERR_TYPE_BAD_REQUEST = "Bad Request";
    public static final String ERR_TYPE_INTERNAL_SERVER = "Internal Server Error";
    
    public static final String LOG_FETCH_START = "Starting data fetch for resource: {}";
    public static final String LOG_FETCH_SUCCESS = "Successfully loaded data for: {}";
    public static final String LOG_APP_STARTUP_LOADING = "Loading data from all strategies";
    public static final String LOG_STRATEGY_REGISTERED = "Registered data fetching strategy: {} for resource: {}";
}
