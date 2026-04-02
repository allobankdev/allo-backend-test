package com.allobank.finance.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface FinanceDataRepository {

    void saveData(String resourceType, List<Map<String, Object>> data);

    Optional<List<Map<String, Object>>> findDataByResourceType(String resourceType);

    void seal();
}
