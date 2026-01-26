package com.chnh16.backendtest.controller;

import com.chnh16.backendtest.exception.CommonException;
import com.chnh16.backendtest.service.DataFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MainController {

    private final Map<String, DataFetcher> fetcherMap;

    @GetMapping("/finance/data/{resourceType}")
    public ResponseEntity<Object> getByResourceType(@PathVariable String resourceType) {
        DataFetcher fetcher = fetcherMap.get(resourceType);
        if(fetcher == null) {
            throw new CommonException("Method not implemented.");
        }
        return ResponseEntity.ok(fetcher.fetch());
    }

}
