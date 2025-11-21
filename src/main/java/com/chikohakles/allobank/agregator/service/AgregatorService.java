package com.chikohakles.allobank.agregator.service;

import com.chikohakles.allobank.agregator.constant.ResourceType;
import com.chikohakles.allobank.agregator.dto.Currency;
import com.chikohakles.allobank.agregator.dto.DateQueryResponse;
import com.chikohakles.allobank.agregator.dto.LatestResponse;

import java.util.Date;
import java.util.List;

public interface AgregatorService {
    public Object getData(String resourceType);
}
