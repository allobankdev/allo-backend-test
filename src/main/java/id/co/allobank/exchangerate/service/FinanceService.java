package id.co.allobank.exchangerate.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import id.co.allobank.exchangerate.common.Constant;
import id.co.allobank.exchangerate.dto.BaseResponseDTO;
import id.co.allobank.exchangerate.exception.CustomException;
import id.co.allobank.exchangerate.store.InMemoryStore;
import id.co.allobank.exchangerate.strategy.FinanceDataStrategy;

@Service
public class FinanceService {

    private final Map<String, FinanceDataStrategy> strategyMap;
    private final InMemoryStore store;

    public FinanceService(List<FinanceDataStrategy> strategies, InMemoryStore store) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        FinanceDataStrategy::getType,
                        s -> s
                ));
        this.store = store;
    }

    public BaseResponseDTO<?> getData(String resourceType) {

        Object cached = store.get(resourceType);

        if (cached != null) {
            return BaseResponseDTO.success(cached);
        }

        FinanceDataStrategy strategy = this.strategyMap.get(resourceType);

        if (strategy == null) {
            throw new CustomException(
                    "Invalid resource",
                    HttpStatus.BAD_REQUEST,
                    Constant.INVALID_REQUEST
            );
        }

        return strategy.fetch();
    }
}