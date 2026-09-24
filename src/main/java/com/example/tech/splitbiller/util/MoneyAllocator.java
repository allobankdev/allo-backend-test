package com.example.tech.splitbiller.util;

import com.example.tech.splitbiller.exception.DataInvalidException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class MoneyAllocator {
    public List<BigDecimal> allocateByWeight(BigDecimal total, List<BigDecimal> quantities) {
        BigDecimal totalQuantity = quantities.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        if (totalQuantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DataInvalidException("Total quantity must be greater than zero");
        }

        List<BigDecimal> result = new ArrayList<>();

        BigDecimal allocated = BigDecimal.ZERO;
        for (BigDecimal quantity : quantities) {
            BigDecimal exactAmount = total.multiply(quantity).divide(totalQuantity, 10, RoundingMode.HALF_UP);
            BigDecimal roundedAmount = exactAmount.setScale(2, RoundingMode.DOWN);
            result.add(roundedAmount);
            allocated = allocated.add(roundedAmount);
        }

        BigDecimal remainder = total.subtract(allocated);
        int cents = remainder.movePointRight(2).intValueExact();

        for (int i = 0; i < cents; i++) {
            result.set(i, result.get(i).add(new BigDecimal("0.01")));
        }

        return result;
    }

    public List<BigDecimal> allocate(BigDecimal total, int numberOfPeople) {
        if (numberOfPeople <= 0) {
            throw new DataInvalidException("Number of people must be greater than zero");
        }

        BigDecimal[] result = new BigDecimal[numberOfPeople];
        BigDecimal base = total.divide(BigDecimal.valueOf(numberOfPeople), 2, RoundingMode.DOWN);
        BigDecimal allocated = base.multiply(BigDecimal.valueOf(numberOfPeople));
        BigDecimal remainder = total.subtract(allocated);

        Arrays.fill(result, base);

        int cents = remainder.movePointRight(2).intValueExact();

        for (int i = 0; i < cents; i++) {
            result[i] = result[i].add(new BigDecimal("0.01"));
        }
        return Arrays.asList(result);
    }
}
