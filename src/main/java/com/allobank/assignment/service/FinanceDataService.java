package com.allobank.assignment.service;

import com.allobank.assignment.model.FinanceDataResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinanceDataService {

    private final FinanceDataCache cache;

    public FinanceDataService(FinanceDataCache cache) {
        this.cache = cache;
    }


    public List<FinanceDataResponse> getFinanceData(String resourceType) {
        return null;
    }
}
