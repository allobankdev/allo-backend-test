package com.example.allo_bank.service.impl;

import com.example.allo_bank.dto.ApiResponse;
import com.example.allo_bank.dto.LatestIdrRatesResponse;
import com.example.allo_bank.integration.dto.LatestIdrRatesDto;
import com.example.allo_bank.service.GetStorageDataService;
import com.example.allo_bank.util.Cache;
import com.example.allo_bank.util.Calculation;
import com.example.allo_bank.util.TypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.allo_bank.util.Constant.SUCCESS;
import static com.example.allo_bank.util.Constant.USD;

@Slf4j
@Service("latest_idr_rates")
public class LatestGetStorageRatesServiceImpl implements GetStorageDataService {

    Logger logger = LoggerFactory.getLogger(LatestGetStorageRatesServiceImpl.class);

    @Autowired
    private Calculation calculation;

    @Autowired
    private Cache cache;

    @Override
    public ApiResponse<Object> fetchData() {

        LatestIdrRatesDto latestIdrRatesDto = cache.getDataCache(TypeEnum.latest_idr_rates);
        BigDecimal usdRate = latestIdrRatesDto.getRates().get(USD);
        BigDecimal usdBuySpreadIdr = calculation.usdBuySpreadIdr(usdRate);
        Map<String, BigDecimal> currencyMap = latestIdrRatesDto.getRates().entrySet().stream()
                .filter(entry -> USD.equals(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        LatestIdrRatesResponse latestIdrRatesResponse = new LatestIdrRatesResponse();
        latestIdrRatesResponse.setBase(latestIdrRatesDto.getBase());
        latestIdrRatesResponse.setDate(latestIdrRatesDto.getDate());
        latestIdrRatesResponse.setRates(currencyMap);
        latestIdrRatesResponse.setUsdBuySpreadIdr(usdBuySpreadIdr);


        ApiResponse <Object> response = new ApiResponse<>();
        response.setData(latestIdrRatesResponse);
        response.setResourceType(String.valueOf(TypeEnum.latest_idr_rates));
        response.setStatus(SUCCESS);

        return response;
    }
}
