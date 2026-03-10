package com.allobank.financeapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    
    @GetMapping("/test")
    public String test() {
        return "✅ Allo Bank API is running with JDK 17!";
    }
    
    @GetMapping("/info")
    public String info() {
        return "Java Version: " + System.getProperty("java.version");
    }
}
