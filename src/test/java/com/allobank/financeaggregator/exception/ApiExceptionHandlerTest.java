package com.allobank.financeaggregator.exception;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

class ApiExceptionHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
    }

    @Test
    void resourceNotFoundReturns404() throws Exception {
        mockMvc.perform(get("/api/test/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Request failed"))
                .andExpect(jsonPath("$.error").value("Unknown resourceType: foo"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void dataNotLoadedReturns503() throws Exception {
        mockMvc.perform(get("/api/test/not-ready"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Request failed"))
                .andExpect(jsonPath("$.error").value("Finance data is not loaded yet"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void externalServiceReturns502() throws Exception {
        mockMvc.perform(get("/api/test/external"))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Request failed"))
                .andExpect(jsonPath("$.error").value("Frankfurter API error: 502"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @RestController
    @RequestMapping("/api/test")
    private static class TestController {

        @GetMapping("/not-found")
        String notFound() {
            throw new ResourceNotFoundException("Unknown resourceType: foo");
        }

        @GetMapping("/not-ready")
        String notReady() {
            throw new DataNotLoadedException("Finance data is not loaded yet");
        }

        @GetMapping("/external")
        String external() {
            throw new ExternalServiceException("Frankfurter API error: 502");
        }
    }
}
