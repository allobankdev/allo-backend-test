package com.allo.test.modules.finance.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Enhanced response for latest IDR exchange rates that includes
 * the calculated USD buy spread with banking margin.
 * <p>
 * Extends the standard LatestRatesResponse to add the personalized
 * USD_BuySpread_IDR field.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LatestIDRRatesResponse extends LatestRatesResponse {

    /**
     * The Rupiah selling rate to USD after applying a banking spread/margin.
     * <p>
     * Calculated using the formula:
     * USD_BuySpread_IDR = (1 / Rate_USD) * (1 + Spread Factor)
     * <p>
     * where Rate_USD is the USD exchange rate from the API when base=IDR,
     * and Spread Factor is derived from the GitHub username.
     */
    @JsonProperty("USD_BuySpread_IDR")
    private BigDecimal usdBuySpreadIdr;

    /**
     * Builder that creates an instance from a base LatestRatesResponse
     * and calculates the USD buy spread.
     */
    @Builder(builderMethodName = "fromLatestRatesResponse")
    public LatestIDRRatesResponse(LatestRatesResponse base, BigDecimal usdBuySpreadIdr) {
        super(base.getAmount(), base.getBase(), base.getDate(), base.getRates());
        this.usdBuySpreadIdr = usdBuySpreadIdr;
    }
}
