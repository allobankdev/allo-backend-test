package com.chikohakles.allobank.agregator.strategy;

import com.chikohakles.allobank.agregator.constant.ResourceType;
import com.chikohakles.allobank.agregator.service.AgregatorService;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class DateQueryStrategy implements BaseStrategy{
    AgregatorService agregatorService;
    DateQueryStrategy(AgregatorService agregatorService) {
        this.agregatorService = agregatorService;
    }
    @Override
    public ResourceType getResourceType() {
        return ResourceType.HISTORICAL_IDR_USD;
    }

    @Override
    public Object getData() {
        String from = "2024-01-01";
        String to = "2024-01-05";
        String base = "IDR";
        String target = "USD";
        Date fromDate = null;
        Date toDate = null;

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            fromDate = dateFormat.parse(from);
            toDate = dateFormat.parse(to);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Unknown date: " + from);
        }

        return agregatorService.getDateQuery(fromDate, toDate, base, target);
    }
}
