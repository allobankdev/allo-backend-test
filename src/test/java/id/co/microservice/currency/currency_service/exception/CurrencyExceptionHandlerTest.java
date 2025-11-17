package id.co.microservice.currency.currency_service.exception;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Example controller to simulate throwing CurrencyException
@RestController
@RequestMapping("/test")
class TestController {
    @GetMapping("/error")
    public String triggerError() {
        throw new CurrencyException("Something went wrong", HttpStatus.NOT_FOUND);
    }
}

@WebMvcTest({TestController.class, CurrencyExceptionHandler.class})
class CurrencyExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testHandleCurrencyException_ReturnsErrorResponse() throws Exception {
        mockMvc.perform(get("/test/error"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Something went wrong"));
    }
}
