package com.ade.exchangerateagregator.domain.service;

import com.ade.exchangerateagregator.application.dto.FinanceBaseResponse;
import com.ade.exchangerateagregator.domain.constant.FinanceConstant;

import java.util.List;

public interface FinanceBaseService {
    FinanceConstant getSourceType();
    List<? extends FinanceBaseResponse> fetchData();
}
