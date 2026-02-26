package org.allobank.com.finanacedataservice.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public class HistoricalIdrUsdResult {
    private final LocalDate date;
    private final BigDecimal usdRate;

    public HistoricalIdrUsdResult(LocalDate date, BigDecimal usdRate) {
        this.date = date;
        this.usdRate = usdRate;
    }

    public LocalDate getDate() {
        return date;
    }

    public BigDecimal getUsdRate() {
        return usdRate;
    }
}
