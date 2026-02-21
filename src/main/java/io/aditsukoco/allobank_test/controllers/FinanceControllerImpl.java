package io.aditsukoco.allobank_test.controllers;

import io.aditsukoco.allobank_test.services.FinanceServiceInterface;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FinanceControllerImpl implements FinanceControllerInterface {

    @Autowired private final FinanceServiceInterface financeService;

    @Override
    @GetMapping("/api/finance/data/{resourceType}")
    public ResponseEntity<?> getFinanceData(@PathVariable String resourceType) {
        return ResponseEntity.ok().build();
    }
}
