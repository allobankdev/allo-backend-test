package com.allobank.splitbill.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class SplitCalculator {
    private static final BigDecimal HUNDRED = new BigDecimal("100.00");
    private static final BigDecimal CENT = new BigDecimal("0.01");

    public Map<UUID, BigDecimal> allocate(BigDecimal amount, SplitType type,
                                           List<UUID> groupParticipants, List<SplitShare> requestedShares) {
        if (amount == null || amount.signum() <= 0 || amount.scale() > 2) {
            throw new IllegalArgumentException("amount must be positive with at most two decimal places");
        }
        if (type == null) {
            throw new IllegalArgumentException("split_type is required");
        }
        BigDecimal total = amount.setScale(2, RoundingMode.UNNECESSARY);
        List<SplitShare> shares = requestedShares == null ? List.of() : requestedShares;
        if (type == SplitType.EQUAL && shares.isEmpty()) {
            shares = groupParticipants.stream().map(id -> new SplitShare(id, null, null)).toList();
        }
        if (shares.isEmpty()) {
            throw new IllegalArgumentException("shares must contain at least one participant");
        }
        Set<UUID> allowed = new HashSet<>(groupParticipants);
        Set<UUID> seen = new HashSet<>();
        for (SplitShare share : shares) {
            if (share == null || share.participantId() == null || !allowed.contains(share.participantId())) {
                throw new IllegalArgumentException("every share must reference a participant in this group");
            }
            if (!seen.add(share.participantId())) {
                throw new IllegalArgumentException("a participant may appear only once in shares");
            }
        }

        return switch (type) {
            case EQUAL -> equal(total, shares);
            case EXACT -> exact(total, shares);
            case PERCENTAGE -> percentage(total, shares);
        };
    }

    private Map<UUID, BigDecimal> equal(BigDecimal total, List<SplitShare> shares) {
        for (SplitShare share : shares) {
            if (share.amount() != null || share.percentage() != null) {
                throw new IllegalArgumentException("EQUAL shares accept participant_id only");
            }
        }
        long cents = total.movePointRight(2).longValueExact();
        long base = cents / shares.size();
        long remainder = cents % shares.size();
        Map<UUID, BigDecimal> result = new LinkedHashMap<>();
        for (int i = 0; i < shares.size(); i++) {
            result.put(shares.get(i).participantId(), BigDecimal.valueOf(base + (i < remainder ? 1 : 0), 2));
        }
        return result;
    }

    private Map<UUID, BigDecimal> exact(BigDecimal total, List<SplitShare> shares) {
        Map<UUID, BigDecimal> result = new LinkedHashMap<>();
        BigDecimal sum = BigDecimal.ZERO;
        for (SplitShare share : shares) {
            if (share.percentage() != null || share.amount() == null || share.amount().signum() < 0
                    || share.amount().scale() > 2) {
                throw new IllegalArgumentException("EXACT shares require a nonnegative amount with at most two decimal places");
            }
            BigDecimal value = share.amount().setScale(2, RoundingMode.UNNECESSARY);
            result.put(share.participantId(), value);
            sum = sum.add(value);
        }
        if (sum.compareTo(total) != 0) {
            throw new IllegalArgumentException("EXACT shares must add up to the expense amount");
        }
        return result;
    }

    private Map<UUID, BigDecimal> percentage(BigDecimal total, List<SplitShare> shares) {
        BigDecimal percentageSum = BigDecimal.ZERO;
        List<Portion> portions = new ArrayList<>();
        for (int i = 0; i < shares.size(); i++) {
            SplitShare share = shares.get(i);
            if (share.amount() != null || share.percentage() == null || share.percentage().signum() < 0
                    || share.percentage().scale() > 2) {
                throw new IllegalArgumentException("PERCENTAGE shares require a nonnegative percentage with at most two decimal places");
            }
            percentageSum = percentageSum.add(share.percentage());
            BigDecimal raw = total.multiply(share.percentage()).divide(HUNDRED);
            BigDecimal roundedDown = raw.setScale(2, RoundingMode.DOWN);
            portions.add(new Portion(share.participantId(), roundedDown, raw.subtract(roundedDown), i));
        }
        if (percentageSum.compareTo(HUNDRED) != 0) {
            throw new IllegalArgumentException("PERCENTAGE shares must add up to 100");
        }
        BigDecimal allocated = portions.stream().map(Portion::amount).reduce(BigDecimal.ZERO, BigDecimal::add);
        int remainingCents = total.subtract(allocated).movePointRight(2).intValueExact();
        List<Portion> priority = new ArrayList<>(portions);
        priority.sort(Comparator.comparing(Portion::fraction).reversed().thenComparingInt(Portion::position));
        Set<UUID> extraCent = new HashSet<>();
        for (int i = 0; i < remainingCents; i++) {
            extraCent.add(priority.get(i).participantId());
        }
        Map<UUID, BigDecimal> result = new LinkedHashMap<>();
        for (Portion portion : portions) {
            result.put(portion.participantId(), portion.amount().add(extraCent.contains(portion.participantId()) ? CENT : BigDecimal.ZERO));
        }
        return result;
    }

    private record Portion(UUID participantId, BigDecimal amount, BigDecimal fraction, int position) {}
}
