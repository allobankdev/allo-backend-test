package cory.sakti.Financial.controller;

import cory.sakti.Financial.service.InMemoryDataStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/finance")
@RequiredArgsConstructor
public class FinanceController {
    private final InMemoryDataStoreService dataStore;


    @GetMapping("/{resource}")
    public ResponseEntity<Object> getFinancialData(@PathVariable String resource) {
        // ATOMIC GREEN: Dynamic lookup from our sealed store
        Object data = dataStore.get(resource);

        // Handle missing resource (Constraint: Graceful Error Handling)
        if (data == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Resource '" + resource + "' not found or not initialized."));
        }

        return ResponseEntity.ok(data);
    }
}
