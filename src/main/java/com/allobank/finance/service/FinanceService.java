package com.allobank.finance.service;

import com.allobank.finance.strategy.IDRDataFetcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FinanceService {

    public Optional<List<Map<String, Object>>> getFinanceData(IDRDataFetcher dataFetcher) {
        return dataFetcher.getData();
    }
}
