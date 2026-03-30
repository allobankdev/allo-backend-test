package com.allobank.idrrates.controller;

import com.allobank.idrrates.store.DataStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/finance")
public class FinanceController {

    private static final Logger log = LoggerFactory.getLogger(FinanceController.class);

    @Autowired
    private DataStore dataStore;

    @GetMapping("/data/{resourceType}")
    public ResponseEntity<Object> getData(@PathVariable String resourceType) {
        log.info("Received request for resource type: {}", resourceType);
        Object data = dataStore.get(resourceType);
        if (data == null) {
            log.warn("Resource type not found: {}", resourceType);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Resource type not found: " + resourceType));
        }
        return ResponseEntity.ok(data);
    }
}
