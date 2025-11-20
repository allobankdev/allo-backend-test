package com.allo.idr.service;

import com.allo.idr.enums.ResourceType;

import java.util.List;

public interface IDRDataFetcher {
    ResourceType getType();
    List<?> fetcData();
}
