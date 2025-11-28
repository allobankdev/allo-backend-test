package com.project.alloBank.repository;

public interface IDRDataFetcher {
    String getResourceType();
    Object fetchData();
}
