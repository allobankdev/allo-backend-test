package com.allo.strategy;

import java.util.List;

import com.allo.dto.FinanceResourceResponse;

public interface IDRFetcher {
    String resourceType();
    List<FinanceResourceResponse> fetch();
}
