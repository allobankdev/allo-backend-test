package com.allobank.assignment.strategy;

import com.allobank.assignment.model.FinanceDataResponse;
import com.allobank.assignment.model.ResourceType;

public interface IdrDataFetchStrategy {

    ResourceType supports();

    FinanceDataResponse fetch();
}
