package com.ade.exchangerateagregator.domain.service;

import com.ade.exchangerateagregator.application.dto.in.FinanceBaseResponse;
import com.ade.exchangerateagregator.domain.constant.ResourceType;

import java.util.List;

public interface FinanceBaseService {
    ResourceType getSourceType();
    List<? extends FinanceBaseResponse> fetchData();
}
