package com.springboot.test.calculate;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculateBuySpread {

    public int getAsciiSum(){
        int sum = 0;
        String input = "ifane-dev";

        for(char ch : input.toCharArray()){
            if(Character.isLowerCase(ch)){
                sum += (int) ch;
            }
        }
        return sum;
    }

    // Spread Factor = (Sum of Unicode Values % 1000) / 100000.0
    public double getSpreadFactorDerivation(){
        double spreadFactor = (getAsciiSum() % 1000) / 100000.0;
        return spreadFactor;
    }

    // USD_BuySpread_IDR = (1 / Rate_USD) * (1 + Spread Factor)
    public BigDecimal getUSDBuySpreadIDR(BigDecimal rateUSD){
        double buySpread = ((1 / rateUSD.doubleValue()) * (1 + getSpreadFactorDerivation()));
        return BigDecimal.valueOf(buySpread).setScale(0, RoundingMode.UP);
    }

}
