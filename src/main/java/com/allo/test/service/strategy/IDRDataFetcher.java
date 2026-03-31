package com.allo.test.service.strategy;

import java.util.List;

public interface IDRDataFetcher {

    String getType();

    List<?> fetch();

}