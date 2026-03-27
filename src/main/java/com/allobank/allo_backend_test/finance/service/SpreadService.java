package com.allobank.allo_backend_test.finance.service;

public interface SpreadService {
    Double getSpreadFactor();
    Double calculateSpread(Double rate);
}