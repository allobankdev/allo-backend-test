package com.ade.exchangerateagregator.adapter.implementation;

import com.ade.exchangerateagregator.application.dto.FinanceBaseResponse;
import com.ade.exchangerateagregator.domain.constant.FinanceConstant;
import com.ade.exchangerateagregator.domain.service.FinanceBaseService;

import java.util.List;

public class HistorycalService implements FinanceBaseService {
    @Override
    public FinanceConstant getSourceType() {
        return FinanceConstant.historical_idr_usd;
    }

    @Override
    public List<? extends FinanceBaseResponse> fetchData() {
        return null;
    }
}
