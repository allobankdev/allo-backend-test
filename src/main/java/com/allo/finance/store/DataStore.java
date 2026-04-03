
package com.allo.finance.store;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class DataStore {

    private Map<String,Object> data = Map.of();

    public synchronized void setAll(Map<String,Object> d){
        data = Collections.unmodifiableMap(new HashMap<>(d));
    }

    public Object get(String key){
        return data.get(key);
    }
}
