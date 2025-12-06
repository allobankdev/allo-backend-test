package com.bank.allo.repository.inbound;

import java.util.Map;

public interface DataStore {

    void initialize(Map<String, Object> data);

    Object get(String key);

    boolean isInitialized();
}
