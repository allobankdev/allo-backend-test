package com.allo.bank.service;

import java.util.List;

import com.allo.bank.dto.FinanceDataItem;

public interface FinanceDataService {

    List<FinanceDataItem> getByResourceType(String resourceType);
}
