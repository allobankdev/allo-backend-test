package id.co.evan.project.aggregator.controller;

import id.co.evan.project.aggregator.model.response.UnifiedFinanceResponse;
import id.co.evan.project.aggregator.fault.ResourceNotFoundException;
import id.co.evan.project.aggregator.service.DataStoreService;
import id.co.evan.project.aggregator.util.ErrorCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/finance/data")
@RequiredArgsConstructor
@Validated
public class FinanceController {
    private final DataStoreService dataStoreService;

    @GetMapping("/{resourceType}")
    public Mono<ResponseEntity<UnifiedFinanceResponse>> getFinanceData(
        @PathVariable
        @NotBlank
        @Pattern(regexp = "^(latest_idr_rates|historical_idr_usd|supported_currencies)$", message = "Resource type tidak dikenal")
        String resourceType
    ) {

        if (!dataStoreService.isReady()) return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build());

        return dataStoreService.getData(resourceType)
            .map(ResponseEntity::ok)
            .switchIfEmpty(Mono.error(new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND)));
    }
}