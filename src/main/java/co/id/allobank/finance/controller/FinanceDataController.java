package co.id.allobank.finance.controller;

import co.id.allobank.finance.service.FinanceDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/finance/data")
@RequiredArgsConstructor
public class FinanceDataController {

    private final FinanceDataService service;

    @GetMapping("/{resourceType}")
    public ResponseEntity<Object> get(@PathVariable String resourceType){
        return ResponseEntity.ok(
                service.getData(resourceType));
    }
}
