package com.example.tech.splitbiller.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

public record DetailSettlementResponse(
        List<SettlementResponse> settlements,
        @JsonProperty("service_charge_pct")
        BigDecimal serviceChargePct,
        @JsonProperty("service_charge_amount")
        BigDecimal serviceChargeAmount
) {}
