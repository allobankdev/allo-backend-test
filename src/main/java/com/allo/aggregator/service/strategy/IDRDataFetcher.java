package com.allo.aggregator.service.strategy;

/**
 * Strategy interface for fetching Finance data.
 * Adheres to Constraint A.
 */
public interface IDRDataFetcher {
  /**
   * @return The data fetched and processed by this strategy.
   */
  Object fetchData();

  /**
   * @return The resource type identifier this strategy handles.
   */
  String getResourceType();
}
