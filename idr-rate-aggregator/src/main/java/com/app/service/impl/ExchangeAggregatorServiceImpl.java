package com.app.service.impl;

import com.app.dao.ExchangeAggregatorDao;
import com.app.model.ExchangeAggregator;
import com.app.service.ExchangeAggregatorService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ExchangeAggregatorServiceImpl implements ExchangeAggregatorService {
    private final ExchangeAggregatorDao exchangeAggregatorDao;

    public ExchangeAggregatorServiceImpl(ExchangeAggregatorDao exchangeAggregatorDao) {
        this.exchangeAggregatorDao = exchangeAggregatorDao;
    }

    @Override
    public void save(ExchangeAggregator exchangeAggregator) {
        exchangeAggregatorDao.save(exchangeAggregator);
    }

    @Override
    public ExchangeAggregator findById(String id) {
        Optional<ExchangeAggregator> resource =  exchangeAggregatorDao.findById(id);

        return resource.get();
    }
}
