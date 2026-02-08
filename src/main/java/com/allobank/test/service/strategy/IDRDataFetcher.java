package com.allobank.test.service.strategy;

public interface IDRDataFetcher {
  Object fetchData();

  String getResourceType();
}