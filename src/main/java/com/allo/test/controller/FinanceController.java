package com.allo.test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceController {

    @GetMapping("/resourceType")
    public String getData(
            @PathVariable String resourceType){
        return "Requested: " + resourceType;
    }
}
