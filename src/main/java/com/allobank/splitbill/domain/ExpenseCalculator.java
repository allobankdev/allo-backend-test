package com.allobank.splitbill.domain;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class ExpenseCalculator {
    private static final BigDecimal ZERO = new BigDecimal("0.00");
    private final SplitCalculator splitCalculator = new SplitCalculator();
    private final TaxCalculator taxCalculator = new TaxCalculator();

    public record ShareAllocation(BigDecimal subtotal, BigDecimal taxAmount, BigDecimal amount) {}
    public record Result(TaxCalculator.Result totals, Map<UUID, ShareAllocation> shares) {}

    public Result calculate(BigDecimal inputAmount, TaxMode taxMode, BigDecimal taxRatePct,
                            SplitType splitType, List<UUID> groupParticipants,
                            List<SplitShare> requestedShares) {
        TaxCalculator.Result totals = taxCalculator.calculate(inputAmount, taxMode, taxRatePct);
        Map<UUID, BigDecimal> inputShares = splitCalculator.allocate(
                totals.inputAmount(), splitType, groupParticipants, requestedShares);
        Map<UUID, BigDecimal> taxShares = allocateProportionally(totals.taxAmount(), inputShares);
        Map<UUID, ShareAllocation> result = new LinkedHashMap<>();

        inputShares.forEach((participantId, inputShare) -> {
            BigDecimal taxShare = taxShares.get(participantId);
            BigDecimal subtotal = taxMode == TaxMode.INCLUSIVE
                    ? inputShare.subtract(taxShare) : inputShare;
            BigDecimal total = taxMode == TaxMode.EXCLUSIVE
                    ? inputShare.add(taxShare) : inputShare;
            result.put(participantId, new ShareAllocation(subtotal, taxShare, total));
        });

        return new Result(totals, Collections.unmodifiableMap(result));
    }

    private Map<UUID, BigDecimal> allocateProportionally(BigDecimal amount,
                                                          Map<UUID, BigDecimal> weights) {
        Map<UUID, BigDecimal> result = new LinkedHashMap<>();
        if (amount.signum() == 0) {
            weights.keySet().forEach(participantId -> result.put(participantId, ZERO));
            return result;
        }

        BigInteger amountCents = amount.movePointRight(2).toBigIntegerExact();
        BigInteger totalWeight = weights.values().stream()
                .map(value -> value.movePointRight(2).toBigIntegerExact())
                .reduce(BigInteger.ZERO, BigInteger::add);
        List<Portion> portions = new ArrayList<>();
        BigInteger allocated = BigInteger.ZERO;
        int position = 0;
        for (Map.Entry<UUID, BigDecimal> entry : weights.entrySet()) {
            BigInteger weight = entry.getValue().movePointRight(2).toBigIntegerExact();
            BigInteger[] division = amountCents.multiply(weight).divideAndRemainder(totalWeight);
            portions.add(new Portion(entry.getKey(), division[0], division[1], position++));
            allocated = allocated.add(division[0]);
        }

        int remainingCents = amountCents.subtract(allocated).intValueExact();
        List<Portion> priority = new ArrayList<>(portions);
        priority.sort(Comparator.comparing(Portion::remainder).reversed()
                .thenComparingInt(Portion::position));
        for (int index = 0; index < remainingCents; index++) {
            priority.get(index).addCent();
        }
        portions.forEach(portion -> result.put(portion.participantId(),
                new BigDecimal(portion.cents(), 2)));
        return result;
    }

    private static final class Portion {
        private final UUID participantId;
        private BigInteger cents;
        private final BigInteger remainder;
        private final int position;

        private Portion(UUID participantId, BigInteger cents, BigInteger remainder, int position) {
            this.participantId = participantId;
            this.cents = cents;
            this.remainder = remainder;
            this.position = position;
        }

        private UUID participantId() {
            return participantId;
        }

        private BigInteger cents() {
            return cents;
        }

        private BigInteger remainder() {
            return remainder;
        }

        private int position() {
            return position;
        }

        private void addCent() {
            cents = cents.add(BigInteger.ONE);
        }
    }
}
