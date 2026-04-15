package id.co.allobank.exchangerate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import id.co.allobank.exchangerate.dto.BaseResponseDTO;
import id.co.allobank.exchangerate.service.FinanceService;

@RestController
@RequestMapping("/api/finance")
public class FinanceController {

    private final FinanceService service;

    public FinanceController(FinanceService service) {
        this.service = service;
    }

    @GetMapping("/data/{type}")
    public BaseResponseDTO<?> get(@PathVariable String type) {
        return service.getData(type);
    }
}
