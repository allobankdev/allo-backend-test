package com.ade.exchangerateagregator.application.service;

import com.ade.exchangerateagregator.application.dto.FinanceBaseResponse;
import com.ade.exchangerateagregator.domain.constant.FinanceConstant;
import com.ade.exchangerateagregator.domain.service.FinanceBaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FinanceService {

    private final Map<FinanceConstant, FinanceBaseService> fetchers;

    public FinanceService(List<FinanceBaseService> fetcher) {
        this.fetchers = fetcher.stream().collect(Collectors.toMap(
                FinanceBaseService::getSourceType,
                Function.identity()
        ));
    }

    public List<? extends FinanceBaseResponse> getFinanceData(String resourceType){
        var type = FinanceConstant.valueOf(resourceType);
        FinanceBaseService fetcher = fetchers.get(type);
        if (fetcher==null) throw new IllegalArgumentException("Unsupported resource type: " + resourceType);
        return fetcher.fetchData();
    }
}
