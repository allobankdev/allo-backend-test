package com.finance.service.fetchers;

import java.util.List;
import java.util.Map;

public interface DataFetcher<T> {
    String resourceType();
    List<T> fetch();
}
