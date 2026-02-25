package com.allobank.finance.repository;

import com.allobank.finance.entity.FinanceDataCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FinanceDataCacheRepository extends JpaRepository<FinanceDataCache, Long> {
    Optional<FinanceDataCache> findByResourceType(String resourceType);
}
