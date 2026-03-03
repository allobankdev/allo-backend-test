package com.ade.exchangerateagregator.adapter.implementation;

import com.ade.exchangerateagregator.application.dto.FinanceBaseResponse;
import com.ade.exchangerateagregator.domain.constant.FinanceConstant;
import com.ade.exchangerateagregator.domain.service.FinanceBaseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LastIdrRatesService implements FinanceBaseService {
    @Override
    public FinanceConstant getSourceType() {
        return FinanceConstant.latest_idr_rates;
    }

    @Override
    public List<? extends FinanceBaseResponse> fetchData() {
        return null;
    }
}
