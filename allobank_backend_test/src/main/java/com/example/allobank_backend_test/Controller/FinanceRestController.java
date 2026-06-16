package com.example.allobank_backend_test.Controller;

import com.example.allobank_backend_test.Service.DataStoreService;
import com.example.allobank_backend_test.Service.IDRDataFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/finance/data")
@RequiredArgsConstructor
public class FinanceRestController {
    private final Map<String, IDRDataFetcher> strategies;
    private final DataStoreService dataStoreService;

    @GetMapping("/{resourceType}")
    public ResponseEntity<?> getData(@PathVariable String resourceType) {
        var data = dataStoreService.get(resourceType);

        if (data == null) {
            return ResponseEntity.badRequest()
                    .body("Invalid resource type: " + resourceType);
        }

        return ResponseEntity.ok(data);
    }
}
