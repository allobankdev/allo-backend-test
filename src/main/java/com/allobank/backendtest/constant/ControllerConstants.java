package com.allobank.backendtest.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ControllerConstants {
    public static final String FINANCE_BASE_URL = "/api/finance";
    public static final String DATA_ENDPOINT = "/data/{resourceType}";
    public static final String RESOURCE_TYPE_VAR = "resourceType";
}
