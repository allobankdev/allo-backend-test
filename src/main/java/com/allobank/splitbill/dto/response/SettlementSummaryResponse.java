package com.allobank.splitbill.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SettlementSummaryResponse {

    private Long groupId;
    private String groupName;
    private BigDecimal totalGroupExpenses;

    @JsonProperty("service_charge_pct")
    private Integer serviceChargePct;

    @JsonProperty("service_charge_amount")
    private BigDecimal serviceChargeAmount;

    private List<ParticipantBalanceResponse> participantBalances;
    private List<SettlementTransactionResponse> settlements;
}
