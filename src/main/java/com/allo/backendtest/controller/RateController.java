package com.allo.backendtest.controller;

import com.allo.backendtest.dto.ResponseWrapper;
import com.allo.backendtest.exception.HttpException;
import com.allo.backendtest.service.IdrDataFetcher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/finance/data")
public class RateController {

    private final Map<String, IdrDataFetcher> idrDataFetcherMap;

    public RateController(Map<String, IdrDataFetcher> idrDataFetcherMap) {
        this.idrDataFetcherMap = idrDataFetcherMap;
    }

    @GetMapping("/{resourceType}")
    public ResponseEntity<ResponseWrapper> getRate(@PathVariable("resourceType") String resourceType) throws HttpException {
        ResponseWrapper response = new ResponseWrapper();

        IdrDataFetcher service = idrDataFetcherMap.get(resourceType.toLowerCase());
        if(service == null) throw new HttpException(404,"Resource type '" + resourceType + "' not found.");

        try{
            response.setData(service.fetch());
            return ResponseEntity.ok(response);
        }catch (Exception e){
            throw new HttpException(500,e.getMessage());
        }
    }

}
