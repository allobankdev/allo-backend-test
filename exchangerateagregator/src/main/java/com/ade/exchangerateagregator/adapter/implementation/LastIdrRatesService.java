package com.ade.exchangerateagregator.adapter.implementation;

import com.ade.exchangerateagregator.adapter.out.external.ExternalServiceImpl;
import com.ade.exchangerateagregator.application.dto.in.FinanceBaseResponse;
import com.ade.exchangerateagregator.application.dto.in.LatesIdrRatesResponse;
import com.ade.exchangerateagregator.application.dto.out.LatesIdrRateExternalResponse;
import com.ade.exchangerateagregator.domain.constant.ResourceType;
import com.ade.exchangerateagregator.domain.service.FinanceBaseService;
import com.ade.exchangerateagregator.util.NumberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LastIdrRatesService implements FinanceBaseService {
    private final ExternalServiceImpl latestIdrRateExternalService;
    @Override
    public ResourceType getSourceType() {
        return ResourceType.latest_idr_rates;
    }

    @Override
    public List<? extends FinanceBaseResponse> fetchData() {
        var currency = "IDR";
        var externalResponse = latestIdrRateExternalService.getLatesIdrRate(currency);
        return mappingResponseExternal(externalResponse);
    }

    private List<? extends FinanceBaseResponse> mappingResponseExternal(LatesIdrRateExternalResponse response){
        List<FinanceBaseResponse> responses = new ArrayList<>();
        var sumChar = "aderadia".chars().sum();
        var spreadFactor = (sumChar%1000) / 100000.0;
        var usdRate = response.getRates().get("USD").doubleValue();
        var usdBuySpreadIdr = BigDecimal.valueOf((1/usdRate) * (1 + spreadFactor));

        responses.add(LatesIdrRatesResponse.builder()
                .amount(response.getAmount())
                .baseCurrency(response.getBaseCurrency())
                .date(response.getDate())
                .rates(response.getRates())
                .USD_BuySpread_IDR(NumberUtil.formatIDR(usdBuySpreadIdr))
                .build());

        return responses;
    }
}
