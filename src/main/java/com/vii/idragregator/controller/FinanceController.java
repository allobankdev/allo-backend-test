package com.vii.idragregator.controller;

import com.vii.idragregator.dto.BaseResponse;
import com.vii.idragregator.service.FinanceDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Luthfi Aryarizki
 * @date Created on 2026/02/14 at 09:00 p.m
 */
@RestController
@RequestMapping("/api/finance")
@Slf4j
public class FinanceController {

    @Autowired
    private FinanceDataService dataService;

    @GetMapping("/data/{resourceType}")
    public ResponseEntity<Object> getFinanceData(@PathVariable String resourceType) {
        log.info("request received for resource: {}", resourceType);
        Object data = dataService.getData(resourceType);

        if (data == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponse.error("Resource not found in memory: " + resourceType));
        }
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success(data));
    }
}
