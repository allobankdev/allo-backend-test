package com.test.frankfurterexchangerate.service;

import java.util.List;
import java.util.Objects;

public interface IDRDataFetcher {
    List<Object> fetchData() throws Exception;
}
