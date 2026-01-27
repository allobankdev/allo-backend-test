package allobankdev.test.finance.controller;

import allobankdev.test.finance.service.FinanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/finance")
public class FinanceController {

    private final FinanceService service;

    public FinanceController(FinanceService service) {
        this.service = service;
    }

    @GetMapping("/data/{resourceType}")
    public ResponseEntity<List<Object>> getData(
            @PathVariable String resourceType) {

        return ResponseEntity.ok(service.getData(resourceType)
        );
    }
}


