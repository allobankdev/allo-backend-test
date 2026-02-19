package co.id.allobank.finance.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryFinanceStore {

    private Map<String,Object> store = new ConcurrentHashMap<>();

    public void put(String key,Object data){
        store.put(key,data);
    }

    public Object get(String key){
        return store.get(key);
    }

    public boolean exists(String key){
        return store.containsKey(key);
    }

    public void makeImmutable(){
        store = Map.copyOf(store);
    }
}
