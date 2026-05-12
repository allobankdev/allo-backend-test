package com.allobankdev.split_bill_api.controller;

import com.allobankdev.split_bill_api.dto.SettlementResponse;
import com.allobankdev.split_bill_api.service.SettlementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/groups/{groupId}/settlement")
@RequiredArgsConstructor
public class SettlementController {

    private final SettlementService
            settlementService;

    @GetMapping
    public SettlementResponse getSettlement(
            @PathVariable Long groupId) {

        return settlementService
                .getSettlement(groupId);
    }
}
