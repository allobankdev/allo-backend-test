package com.allo.backendtest.store;

public interface BaseStore<T> {
    void setData(T data);
    T getData();
}
