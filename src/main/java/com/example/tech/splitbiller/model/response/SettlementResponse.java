package com.example.tech.splitbiller.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record SettlementResponse(
        String fromPersonId,
        String fromPersonName,
        String toPersonId,
        String toPersonName,
        BigDecimal amount
) {}
