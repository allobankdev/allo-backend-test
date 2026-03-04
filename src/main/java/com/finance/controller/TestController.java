package com.finance.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;


@RestController
public class TestController {

    
    private final WebClient webClient;
    
    public TestController(WebClient webClient) {
        this.webClient = webClient;
    }
    
    @GetMapping("/test-client")
    public String test() {
        return "WebClient injected successfully!";
    }
    
}
