package com.example.allo_bank.service.impl;

import com.example.allo_bank.dto.ApiResponse;
import com.example.allo_bank.service.GetStorageDataService;
import com.example.allo_bank.util.Cache;
import com.example.allo_bank.util.TypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.example.allo_bank.util.Constant.SUCCESS;

@Service("supported_currencies")
public class SupportedCurrencies implements GetStorageDataService {

    @Autowired
    private Cache cache;

    @Override
    public ApiResponse<Object> fetchData() {

        Map<String, String> map = cache.getDataCache(TypeEnum.supported_currencies);

        ApiResponse<Object> response = new ApiResponse<>();
        response.setData(map);
        response.setStatus(SUCCESS);
        response.setResourceType(String.valueOf(TypeEnum.supported_currencies));

        return response;
    }
}
