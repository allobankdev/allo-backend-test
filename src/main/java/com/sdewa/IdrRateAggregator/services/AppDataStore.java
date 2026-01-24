package com.sdewa.IdrRateAggregator.services;

public interface AppDataStore {

    public void put(String key, Object value);

    public Object get(String key);

}