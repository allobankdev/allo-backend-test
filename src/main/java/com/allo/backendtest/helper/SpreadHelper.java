package com.allo.backendtest.helper;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
public class SpreadHelper {

//  The Spread Factor Must Be Unique :
//  Input: Your GitHub username (e.g., johndoe47).
//  Calculation: Calculate the sum of the Unicode (ASCII) values of all characters in your lowercase GitHub username string.
//  Spread Factor Derivation: Spread Factor = (Sum of Unicode Values % 1000) / 100000.0 (This will yield a unique factor between 0.00000 and 0.00999, ensuring a personalized result.)
//  Final Formula: USD_BuySpread_IDR = (1 / Rate_USD) * (1 + Spread Factor) (where Rate_USD is the value from the API when base=IDR).
    public static BigDecimal getSpread(String username,BigDecimal rateUsd){
        if(username == null) throw new IllegalArgumentException("Username is null");
        if(rateUsd == null) throw new IllegalArgumentException("Rate USD is null");

        BigDecimal one = BigDecimal.valueOf(1);
        int sum = username.toLowerCase().chars().sum();

        BigDecimal spreadFactor = BigDecimal.valueOf((sum % 1000) / 100000.0);

        BigDecimal oneDivideRateUsd = one.divide(rateUsd, RoundingMode.HALF_EVEN);
        BigDecimal factorPlusOne = one.add(spreadFactor);

        return oneDivideRateUsd.multiply(factorPlusOne);
    }

}
