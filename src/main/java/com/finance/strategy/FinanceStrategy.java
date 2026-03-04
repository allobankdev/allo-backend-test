package com.finance.strategy;

public interface FinanceStrategy {

    String getType();
    
    Object execute();
}
