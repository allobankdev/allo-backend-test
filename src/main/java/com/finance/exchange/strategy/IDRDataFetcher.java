package com.finance.exchange.strategy;

public interface IDRDataFetcher {
  String getResourceType();

  void fetchData();

  Object getData();
}