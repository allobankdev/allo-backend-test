package com.athallah.finance.startegy;

import com.athallah.finance.util.constant.ResourceType;

public interface IDRDataFetcher {
    Object fetchData();
    ResourceType getResourceType();
}