package com.allobank.controller;

import com.allobank.controller.base.BaseController;
import com.allobank.exceptions.BusinessException;
import com.allobank.service.InMemoryDataStore;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static com.allobank.enums.RESPONSE.DATA_NOT_FOUND;

@RestController
@RequestMapping("/api/finance")
@RequiredArgsConstructor
public class FinanceController extends BaseController {
    private final InMemoryDataStore dataStore;

    @GetMapping("/data/{resourceType}")
    public ResponseEntity<?> getData(@PathVariable("resourceType") String resourceType) {
        return createSuccessResponse(Optional.ofNullable(dataStore.get(resourceType))
                .orElseThrow(() -> new BusinessException(DATA_NOT_FOUND)));
    }
}
