package com.ade.exchangerateagregator.adapter.implementation;

import com.ade.exchangerateagregator.application.dto.FinanceBaseResponse;
import com.ade.exchangerateagregator.domain.constant.FinanceConstant;
import com.ade.exchangerateagregator.domain.service.FinanceBaseService;

import java.util.List;

public class SupportedCurrencyService implements FinanceBaseService {
    @Override
    public FinanceConstant getSourceType() {
        return FinanceConstant.supported_currencies;
    }

    @Override
    public List<? extends FinanceBaseResponse> fetchData() {
        return null;
    }
}
