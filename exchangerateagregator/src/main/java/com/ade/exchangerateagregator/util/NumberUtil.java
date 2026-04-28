package com.ade.exchangerateagregator.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public final class NumberUtil {
    public NumberUtil() {
    }

    public static String formatIDR(BigDecimal amount){
        NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("id","ID"));
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        return formatter.format(amount);
    }
}
