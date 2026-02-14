package cory.sakti.Financial.dto;

import java.math.BigDecimal;
import java.util.Map;

public record IDRRateData(
        String base,
        String date,
        Map<String, BigDecimal> rates,
        BigDecimal usdBuySpreadIdr,
        BigDecimal spreadFactorUsed
) {}
