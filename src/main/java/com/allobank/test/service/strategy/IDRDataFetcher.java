package com.allobank.test.service.strategy;

/**
 * Dibuat oleh: Andre Rizaldi Brillianto
 * Email: andrerizaldib@gmail.com
 * Date: Wednesday, 7-January-2026
 * description: allo-bank-test
 */
public interface IDRDataFetcher {

    Object fetchData();

    Object getCachedData();

    String getResourceType();
}
