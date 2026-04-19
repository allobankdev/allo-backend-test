package com.allobank.allobackend.core.domain;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class FinanceDataStore {
    private final Map<String , Object> storage = new ConcurrentHashMap<>();
    private final AtomicBoolean init = new AtomicBoolean(false);

    public void save(String key , Object data){
        if(init.get()) {
            throw new IllegalStateException("Store is imutable");
        }

        if(data instanceof  org.json.JSONObject){
            storage.put(key , ((org.json.JSONObject) data).toMap());
        }else {
            storage.put(key , data);
        }
    }
    public void lock(){ this.init.set(true);}
    public Object get(String key){return storage.get(key);}

}
