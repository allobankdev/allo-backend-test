package com.allobank.splitbill.api.dto.response;

import java.util.List;

public record PersonalBillsResponse(
        UserResponse user,
        List<CurrencyBillSummaryResponse> totalsByCurrency,
        List<GroupBillSummaryResponse> groups
) {
}
