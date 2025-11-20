package com.athallah.finance.service.strategy;

import com.athallah.finance.util.constant.ResourceType;

public interface IDRDataFetcher {
    Object fetchData();
    ResourceType getResourceType();
}