package com.example.AlloBank.strategy;

import java.util.List;

public interface FinanceDataStrategy<T> {

    String getType();
    List<T> getData();

}
