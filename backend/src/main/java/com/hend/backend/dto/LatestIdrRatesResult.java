package com.hend.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author : hend wunga
 */

@Data
@AllArgsConstructor
public class LatestIdrRatesResult {

    private FrankfurterResponse originalData;
    private double usdBuySpreadIdr;
    private double spreadFactor;
}
