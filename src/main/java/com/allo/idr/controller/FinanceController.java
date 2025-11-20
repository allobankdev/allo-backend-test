package com.allo.idr.controller;

import com.allo.idr.cache.ImmutableDataCache;
import com.allo.idr.enums.ResourceType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceController {
    private final ImmutableDataCache cache;

    public FinanceController(ImmutableDataCache cache) {
        this.cache = cache;
    }


    @GetMapping("/{resourceType}")
    public ResponseEntity<?> getData(@PathVariable String resourceType) {
        ResourceType rType = ResourceType.from(resourceType);
        Object data = cache.get(rType);
        System.out.println("request" + rType + ", cache data :" + data);
        return Optional
                .ofNullable(cache.get(rType))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}