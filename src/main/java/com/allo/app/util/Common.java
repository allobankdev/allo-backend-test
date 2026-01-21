package com.allo.app.util;

import java.math.BigDecimal;

public class Common {

    public static BigDecimal calculateSpreadFactor(String name){
        return BigDecimal.valueOf((name.toLowerCase().chars().sum() % 1000) / 100_000.0);
    }
}
