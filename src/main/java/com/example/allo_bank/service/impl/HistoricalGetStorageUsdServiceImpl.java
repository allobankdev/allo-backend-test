package com.example.allo_bank.service.impl;

import com.example.allo_bank.dto.ApiResponse;
import com.example.allo_bank.dto.HistoricalIdrUsdResponse;
import com.example.allo_bank.integration.dto.HistoricalIdrUsdDto;
import com.example.allo_bank.service.GetStorageDataService;
import com.example.allo_bank.util.Cache;
import com.example.allo_bank.util.TypeEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.allo_bank.util.Constant.SUCCESS;

@Service("historical_idr_usd")
public class HistoricalGetStorageUsdServiceImpl implements GetStorageDataService {

    @Autowired
    private Cache cache;

    @Override
    public ApiResponse<Object> fetchData() {

        HistoricalIdrUsdDto historicalIdrUsdDto = cache.getDataCache(TypeEnum.historical_idr_usd);
        HistoricalIdrUsdResponse historicalIdrUsdResponse = new HistoricalIdrUsdResponse();
        BeanUtils.copyProperties(historicalIdrUsdDto, historicalIdrUsdResponse);

        ApiResponse<Object> response = new ApiResponse<>();
        response.setData(historicalIdrUsdResponse);
        response.setStatus(SUCCESS);
        response.setResourceType(String.valueOf(TypeEnum.historical_idr_usd));

        return response;
    }
}
