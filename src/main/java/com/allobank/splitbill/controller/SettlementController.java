package com.allobank.splitbill.controller;

import com.allobank.splitbill.dto.response.SettlementSummaryResponse;
import com.allobank.splitbill.service.SettlementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/groups/{groupId}/settlement")
@RequiredArgsConstructor
@Tag(name = "Settlements", description = "Endpoints for settlement calculation and debt simplification breakdown")
public class SettlementController {

    private final SettlementService settlementService;

    @GetMapping
    @Operation(summary = "Get settlement summary", description = "Calculates net balances, simplified debt settlements (minimizing transactions), and personalization service charge")
    public ResponseEntity<SettlementSummaryResponse> getSettlement(@PathVariable Long groupId) {
        SettlementSummaryResponse summary = settlementService.getSettlementSummary(groupId);
        return ResponseEntity.ok(summary);
    }
}
