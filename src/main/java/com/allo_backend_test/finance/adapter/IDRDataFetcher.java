
package com.allo_backend_test.finance.adapter;

public interface IDRDataFetcher {
    String getResourceType();
    Object fetchAndTransform();
}
