package com.allobank.allobackend.common.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Utils {

    public static double caculateSpreadFactorByUsername(String userName){
        long sum = userName.chars().filter(Character::isLowerCase).sum();
        return (sum % 1000) / 10000.0;
    }

    public static double calculateBuySpread(double rateUsd ,  double spreadFactor){
        return (1/rateUsd) * (1 + spreadFactor);
    }

}
