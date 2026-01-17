package com.example.allobank.backend.test.takehometest.controllers;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
// import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.allobank.backend.test.takehometest.fetcher.DataFetcher;

// import com.example.allobank.backend.dto.ResponseData;


@RestController
@RequestMapping("/api/finance/data")
public class MainController {
    

    private final Map<String, DataFetcher> fetcherMap;

    public MainController(List<DataFetcher> fetch) {
        this.fetcherMap = fetch.stream()
            .collect(Collectors.toMap(
                DataFetcher::getResourceType,
                Function.identity()
            ));
    }

    @GetMapping("/{resourceType}")
    public Object getResource(@PathVariable("resourceType") String resourceType){
        return fetcherMap.get(resourceType).fetchData();
    }
}
