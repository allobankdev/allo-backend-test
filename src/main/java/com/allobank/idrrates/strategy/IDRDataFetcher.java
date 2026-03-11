package com.allobank.idrrates.strategy;

import java.util.List;

public interface IDRDataFetcher {

    String getResourceType();

    List<?> fetchAndTransform();
}
