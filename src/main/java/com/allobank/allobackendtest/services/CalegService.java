package com.allobank.allobackendtest.services;

import java.util.HashMap;

import com.allobank.allobackendtest.responses.ResponseMsg;

public interface CalegService {
    ResponseMsg<HashMap<String, Object>> SearchCaleg(Integer page, Integer size, String orderBy,
            String orderDirection, String dapil, String partai);
}
