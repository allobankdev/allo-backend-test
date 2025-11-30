package com.app.dao;

import com.app.model.ExchangeAggregator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeAggregatorDao extends JpaRepository<ExchangeAggregator, String> {
}
