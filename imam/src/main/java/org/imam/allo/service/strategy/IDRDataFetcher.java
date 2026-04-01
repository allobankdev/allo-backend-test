package org.imam.allo.service.strategy;

public interface IDRDataFetcher {
    String getResourceType();
    Object fetchData();
}
