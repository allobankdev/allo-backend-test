package id.tisnanda.allobank.allo_bank_backend_test.controller;


import id.tisnanda.allobank.allo_bank_backend_test.service.IDRFinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/finance/data")
public class IDRDataController {

    @Autowired
    private IDRFinanceService financeService;

    @GetMapping("/{resourceType}")
    public List<Map<String, Object>> getData(@PathVariable String resourceType) {
        return financeService.getData(resourceType);
    }
}
