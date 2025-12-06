package achlaq.co.allo_backend_test.finance.controller;

import achlaq.co.allo_backend_test.finance.strategy.IdrDataFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/finance/data")
@RequiredArgsConstructor
public class FinanceDataController {

    private final Map<String, IdrDataFetcher> fetchers;

    @GetMapping("/{resourceType}")
    public ResponseEntity<?> getFinanceData(@PathVariable String resourceType) {
        IdrDataFetcher fetcher = fetchers.get(resourceType);
        if (fetcher == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Unsupported resourceType: " + resourceType
            );
        }
        Object data = fetcher.getCachedData();
        if (data == null) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Data not initialized yet for: " + resourceType
            );
        }
        return ResponseEntity.ok(List.of(data));
    }
}

